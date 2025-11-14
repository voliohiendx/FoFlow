package volio.b1.foflow.ui.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.adapter.OnboardingAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import volio.b1.foflow.R
import volio.b1.foflow.utils.setPreventDoubleClick
import androidx.core.view.isNotEmpty
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import volio.b1.foflow.model.OnboardingItemModel

class OnboardingActivity : AppCompatActivity() {
    private lateinit var vpTemplate: ViewPager2
    private lateinit var tvNext: TextView
    private lateinit var tvGetStarted: TextView
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var layoutAds: FrameLayout
    var indexAds = 0
    val adapter by lazy {
        val filteredItemsWithAdsData = FOFlowManager.config.onboarding.items.mapNotNull { item ->
            if (item.type == OnboardingItemModel.TYPE_ADS) {
                indexAds++
                val ns = FOFlowManager.config.onboarding.nameSpaceAdsFull.getOrNull(indexAds - 1)
                    ?: return@mapNotNull null
                val layoutRes =
                    FOFlowManager.config.onboarding.adsLayoutResFull.getOrNull(indexAds - 1)
                        ?: return@mapNotNull null
                if (FOFlowManager.isEnableShowAds(ns)) {
                    item to (ns to layoutRes)
                } else null
            } else {
                item to null
            }
        }

        FOFlowManager.setDataOnboardingItem(filteredItemsWithAdsData.map { it.first })
        OnboardingAdapter(
            items = filteredItemsWithAdsData.map { it.first }, onLoadAds = { view, position ->
                val adData = filteredItemsWithAdsData.getOrNull(position)?.second
                adData?.let { (ns, layoutRes) ->
                    FOFlowManager.callback?.showNativeAds(
                        ns,
                        view,
                        layoutRes,
                        FOFlowManager.config.onboarding.nameTracking
                    )
                }

            }, onNextPage = {
                onNextPage()
            }
        )
    }

    private var autoScrollJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_onboarding)
        hideNavigationBar()
        initView()
        val adContainer = findViewById<FrameLayout>(R.id.layoutAds)

        FOFlowManager.callback?.showNativeAds(
            FOFlowManager.config.onboarding.nameSpaceAds,
            adContainer,
            if (FOFlowManager.isShowDefaultAds(idScreen)) R.layout.native_ads_default else FOFlowManager.config.onboarding.adsLayoutRes,
            FOFlowManager.config.onboarding.nameTracking
        )
        setupViewPage()
        initListener()

    }

    private fun initView() {
        vpTemplate = findViewById(R.id.vpTemplate)
        tvNext = findViewById(R.id.tvNext)
        tvGetStarted = findViewById(R.id.tvGetStarted)
        dotsIndicator = findViewById(R.id.dots_indicator)
        layoutAds = findViewById(R.id.layoutAds)
    }

    private fun setupViewPage() {
        val vpTemplate = findViewById<ViewPager2>(R.id.vpTemplate)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)

        vpTemplate.adapter = adapter

        dotsIndicator.attachTo(vpTemplate)
    }

    private fun initListener() {
        val vpTemplate = findViewById<ViewPager2>(R.id.vpTemplate)
        val tvNext = findViewById<TextView>(R.id.tvNext)
        val tvGetStarted = findViewById<TextView>(R.id.tvGetStarted)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        val layoutAds = findViewById<FrameLayout>(R.id.layoutAds)

        tvGetStarted.visibility = View.INVISIBLE

        tvNext.setPreventDoubleClick {
            if (vpTemplate.currentItem == adapter.itemCount - 1) navigateNext()
            else vpTemplate.currentItem++
        }

        tvGetStarted.setPreventDoubleClick { navigateNext() }

        vpTemplate.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val currentItem = FOFlowManager.config.onboarding.items[position]
                layoutAds.visibility =
                    if (layoutAds.isNotEmpty()) currentItem.adsVisibility else View.GONE

                autoScrollJob?.cancel()

                val isAds = currentItem.type == OnboardingItemModel.TYPE_ADS
                val isLast = position == adapter.itemCount - 1

                if (isAds) {
                    val delayMs = currentItem.timeDelayNextScreenAdsFull
                    if (delayMs > 0) {
                        autoScrollJob = CoroutineScope(Dispatchers.IO).launch {
                            delay(delayMs)
                            if (position < adapter.itemCount - 1) {
                                withContext(Dispatchers.Main) {
                                    vpTemplate.setCurrentItem(position + 1, true)
                                }
                            }
                        }
                    }
                }

                dotsIndicator.visibility = if (isAds) View.INVISIBLE else View.VISIBLE
                tvGetStarted.visibility = if (!isAds && isLast) View.VISIBLE else View.INVISIBLE
                tvNext.visibility = if (!isAds && !isLast) View.VISIBLE else View.INVISIBLE
            }
        })

        onBackPressedDispatcher.addCallback(this, true) {}
    }

    fun onNextPage() {
        if (vpTemplate.currentItem == adapter.itemCount - 1) navigateNext()
        else vpTemplate.currentItem++
    }

    fun navigateNext() {
        val isShowOnlyScreen = intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false
        val goNext: () -> Unit = {
            FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
            finish()
        }

        if (FOFlowManager.config.onboarding.showAdsInter) {
            FOFlowManager.callback?.showInterAds(lifecycle, goNext)
        } else goNext()
    }

    override fun onResume() {
        super.onResume()
        if (FOFlowManager.config.onboarding.nameTracking.isNotBlank()) FOFlowManager.callback?.pushTracking(
            true, FOFlowManager.config.onboarding.nameTracking
        )
    }

    override fun onPause() {
        super.onPause()
        if (FOFlowManager.config.onboarding.nameTracking.isNotBlank()) FOFlowManager.callback?.pushTracking(
            false, FOFlowManager.config.onboarding.nameTracking
        )
    }

    private fun hideNavigationBar() {
        WindowCompat.getInsetsController(window, findViewById(R.id.layoutRoot)).let {
            it.hide(WindowInsetsCompat.Type.navigationBars())
            it.hide(WindowInsetsCompat.Type.statusBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutRoot)) { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }
    }

    companion object {
        val idScreen = "onboarding"
        val isShowOnlyScreen = "isShowOnlyScreen"
    }
}
