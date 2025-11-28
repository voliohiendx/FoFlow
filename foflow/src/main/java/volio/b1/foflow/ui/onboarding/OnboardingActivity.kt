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
import androidx.viewpager2.widget.ViewPager2
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.adapter.OnboardingAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import volio.b1.foflow.R
import volio.b1.foflow.utils.setPreventDoubleClick
import androidx.core.view.isNotEmpty
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
    val adapter by lazy {
        val filteredItems = FOFlowManager.config.onboarding.items.filter { item ->
            if (item.type == OnboardingItemModel.TYPE_ADS) {
                FOFlowManager.isEnableShowAds(item.adsData?.spaceAds ?: "")
            } else {
                true
            }
        }

        FOFlowManager.setDataOnboardingItem(filteredItems)

        OnboardingAdapter(
            items = filteredItems,
            onLoadAds = { view, item ->
                item.adsData?.let {
                    FOFlowManager.callback?.showNativeAds(
                        it.spaceAds,
                        view,
                        it.layoutAds,
                        FOFlowManager.config.onboarding.nameTracking
                    )
                }
            },

            onNextPage = {
                onNextPage()
            }
        )
    }

    private var autoScrollJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(FOFlowManager.config.onboarding.onboardingLayoutRes)
        hideNavigationBar()
        initView()
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
        vpTemplate.offscreenPageLimit = 10

        dotsIndicator.attachTo(vpTemplate)
    }

    private fun initListener() {
        tvGetStarted.visibility = View.INVISIBLE

        tvNext.setPreventDoubleClick {
            if (vpTemplate.currentItem == adapter.itemCount - 1) navigateNext()
            else vpTemplate.currentItem++
        }

        tvGetStarted.setPreventDoubleClick { navigateNext() }

        vpTemplate.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                FOFlowManager.callback?.scrollPageOnboarding(position, adapter.items[position])
                loadAds(position)
            }
        })

        layoutAds.post {
            loadAds(0)
        }
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

    fun loadAds(position: Int) {
        val currentItem = FOFlowManager.config.onboarding.items[position]
        val isAds = currentItem.type == OnboardingItemModel.TYPE_ADS
        val isLast = position == adapter.itemCount - 1
        layoutAds.visibility =
            if (layoutAds.isNotEmpty()) currentItem.adsVisibility else View.GONE

        currentItem.adsData?.let { adsData ->
            autoScrollJob?.cancel()

            if (isAds) {
                layoutAds.visibility = View.GONE
                val delayMs = adsData.timeDelayNextScreenAdsFull
                if (delayMs > 0) {
                    autoScrollJob = CoroutineScope(Dispatchers.IO).launch {
                        delay(delayMs)
                        if (position < adapter.itemCount - 1) {
                            withContext(Dispatchers.Main) {
                                vpTemplate.setCurrentItem(position + 1, true)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                navigateNext()
                            }
                        }
                    }
                }
            } else {
                if (adsData.spaceAds != "") {
                    if (FOFlowManager.isEnableShowAds(adsData.spaceAds)) {
                        layoutAds.visibility = View.VISIBLE
                        FOFlowManager.callback?.showNativeAds(
                            adsData.spaceAds,
                            layoutAds,
                            adsData.layoutAds,
                            FOFlowManager.config.onboarding.nameTracking
                        )
                    }
                }
            }
        }

        dotsIndicator.visibility = if (isAds) View.INVISIBLE else View.VISIBLE
        tvGetStarted.visibility = if (!isAds && isLast) View.VISIBLE else View.INVISIBLE
        tvNext.visibility = if (!isAds && !isLast) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        val idScreen = "onboarding"
        val isShowOnlyScreen = "isShowOnlyScreen"
    }
}
