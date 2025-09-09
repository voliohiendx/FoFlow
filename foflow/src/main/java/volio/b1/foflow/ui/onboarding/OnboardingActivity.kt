package volio.b1.foflow.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.adapter.OnboardingAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import volio.b1.foflow.R
import volio.b1.foflow.config.OnboardingConfig
import volio.b1.foflow.utils.setPreventDoubleClick

class OnboardingActivity : AppCompatActivity() {

    val adapter by lazy {
        OnboardingAdapter(
            items = OnboardingConfig.items,
            itemLayoutRes = OnboardingConfig.itemLayoutRes,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = OnboardingConfig.activityLayoutRes
        setContentView(layoutId)
        hideNavigationBar()
        val adContainer = findViewById<FrameLayout>(R.id.layoutAds)

        FOFlowManager.showNativeAds.invoke(
            OnboardingConfig.nameSpaceAds,
            adContainer,
            if (FOFlowManager.isShowDefaultAds(idScreen)) OnboardingConfig.adsLayoutResDefault else OnboardingConfig.adsLayoutRes
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
        val layoutAds = findViewById<FrameLayout>(R.id.layoutAds)
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

        vpTemplate.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val isShowAds = OnboardingConfig.items[position].isShowAds
                if (!isShowAds) {
                    if (layoutAds.isVisible) {
                        layoutAds.visibility = View.INVISIBLE
                    }
                } else {
                    if (layoutAds.isInvisible) {
                        layoutAds.visibility = View.VISIBLE
                    }
                }

            }
        })
        this.onBackPressedDispatcher.addCallback(this, true) {
        }
    }

    override fun onResume() {
        super.onResume()
        if (OnboardingConfig.nameTracking.isNotBlank()) FOFlowManager.pushTracking.invoke(
            true,
            OnboardingConfig.nameTracking
        )
    }

    override fun onPause() {
        super.onPause()
        if (OnboardingConfig.nameTracking.isNotBlank()) FOFlowManager.pushTracking.invoke(
            false,
            OnboardingConfig.nameTracking
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