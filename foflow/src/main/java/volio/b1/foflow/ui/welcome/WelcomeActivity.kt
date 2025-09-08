package volio.b1.foflow.ui.welcome

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import c.b1.fo.FOFlowManager
import volio.b1.foflow.R
import volio.b1.foflow.config.WelcomeConfig

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutId = WelcomeConfig.activityLayoutRes
        setContentView(layoutId)
        hideNavigationBar()

        val adContainer = findViewById<FrameLayout>(R.id.layoutAds)
        FOFlowManager.showNativeAds.invoke(
            WelcomeConfig.nameSpaceAds,
            adContainer,
            if (FOFlowManager.isShowDefaultAds(idScreen)) WelcomeConfig.adsLayoutResDefault else WelcomeConfig.adsLayoutRes
        )
        initListener()

    }

    fun initListener() {
        val tvStart = findViewById<TextView>(R.id.tvStart)
        tvStart?.setOnClickListener {
            val isShowOnlyScreen =
                intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false

            FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (WelcomeConfig.nameTracking.isNotBlank())
            FOFlowManager.pushTracking.invoke(true, WelcomeConfig.nameTracking)
    }

    override fun onPause() {
        super.onPause()
        if (WelcomeConfig.nameTracking.isNotBlank())
            FOFlowManager.pushTracking.invoke(false, WelcomeConfig.nameTracking)
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
        val idScreen = "welcome"
        val isShowOnlyScreen = "isShowOnlyScreen"
    }
}