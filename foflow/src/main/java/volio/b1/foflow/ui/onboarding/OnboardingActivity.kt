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

    val adapter by lazy {
        val filteredItems = FOFlowManager.config.onboarding.items.filter { item ->
            if (item.type == OnboardingItemModel.TYPE_ADS) {
                FOFlowManager.isEnableShowAdsBySpaceName(FOFlowManager.config.onboarding.nameSpaceAdsFull)
            } else {
                true
            }
        }

        OnboardingAdapter(
            items = filteredItems
        ) { view ->
            FOFlowManager.callback?.showNativeAds(
                FOFlowManager.config.onboarding.nameSpaceAdsFull,
                view,
                if (FOFlowManager.isShowDefaultAds(idScreen))
                    R.layout.native_ads_default
                else
                    FOFlowManager.config.onboarding.adsLayoutResFull,
                FOFlowManager.config.onboarding.nameTracking
            )
        }
    }

    private var autoScrollJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_onboarding)
        hideNavigationBar()
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

    private fun setupViewPage() {
        val vpTemplate = findViewById<ViewPager2>(R.id.vpTemplate)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)

        vpTemplate.adapter = adapter

        dotsIndicator.attachTo(vpTemplate)
    }

    fun initListener() {
        val vpTemplate = findViewById<ViewPager2>(R.id.vpTemplate)
        val tvNext = findViewById<TextView>(R.id.tvNext)
        val tvGetStarted: TextView? = findViewById<TextView>(R.id.tvGetStarted)
        val layoutAds = findViewById<FrameLayout>(R.id.layoutAds)
        tvGetStarted?.visibility = View.INVISIBLE

        tvNext.setPreventDoubleClick {
            if (vpTemplate.currentItem == adapter.itemCount - 1) {
                val isShowOnlyScreen =
                    intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false

                FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
                finish()
            } else {
                vpTemplate.currentItem += 1
            }
        }

        tvGetStarted?.setPreventDoubleClick {
            val isShowOnlyScreen =
                intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false

            FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
            finish()

        }

        vpTemplate.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (layoutAds.isNotEmpty()) {
                    layoutAds.visibility =
                        FOFlowManager.config.onboarding.items[position].adsVisibility
                } else {
                    layoutAds.visibility = View.GONE
                }

                autoScrollJob?.cancel()

                if (FOFlowManager.config.onboarding.items[position].type == OnboardingItemModel.TYPE_ADS) {
                    val timeDelayNextScreen =
                        FOFlowManager.config.onboarding.items[position].timeDelayNextScreenAdsFull
                    if (timeDelayNextScreen > 0) {
                        autoScrollJob = CoroutineScope(Dispatchers.IO).launch {
                            delay(timeDelayNextScreen)
                            if (position < adapter.itemCount - 1) {
                                withContext(Dispatchers.Main) {
                                    vpTemplate.setCurrentItem(position + 1, true)
                                }
                            }
                        }
                    }

                    tvGetStarted?.visibility = View.INVISIBLE
                    tvNext.visibility = View.INVISIBLE
                } else {
                    if (position == adapter.itemCount - 1) {
                        if (tvGetStarted != null) {
                            tvGetStarted.visibility = View.VISIBLE
                            tvNext.visibility = View.INVISIBLE
                        }
                    } else {
                        if (tvGetStarted != null) {
                            tvGetStarted.visibility = View.INVISIBLE
                            tvNext.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
        this.onBackPressedDispatcher.addCallback(this, true) {
        }
    }

    override fun onResume() {
        super.onResume()
        if (FOFlowManager.config.onboarding.nameTracking.isNotBlank()) FOFlowManager.callback?.pushTracking(
            true,
            FOFlowManager.config.onboarding.nameTracking
        )
    }

    override fun onPause() {
        super.onPause()
        if (FOFlowManager.config.onboarding.nameTracking.isNotBlank()) FOFlowManager.callback?.pushTracking(
            false,
            FOFlowManager.config.onboarding.nameTracking
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
