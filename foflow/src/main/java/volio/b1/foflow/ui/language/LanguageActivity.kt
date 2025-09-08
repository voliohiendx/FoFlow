package volio.b1.foflow.ui.language

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import c.b1.fo.FOFlowManager
import volio.b1.foflow.adapter.LanguageAdapter
import volio.b1.foflow.config.LanguageConfig
import volio.b1.foflow.R

class LanguageActivity : AppCompatActivity() {
    var code = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = LanguageConfig.activityLayoutRes
        setContentView(layoutId)
        hideNavigationBar()

        val isShowOnlyScreen = intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false

        val adContainer = findViewById<FrameLayout>(R.id.layoutAds)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLanguage)
        val btnNext = findViewById<TextView>(R.id.tvSelect)

        FOFlowManager.showNativeAds.invoke(
            LanguageConfig.nameSpaceAds,
            adContainer,
            if (FOFlowManager.isShowDefaultAds(idScreen)) LanguageConfig.adsLayoutResDefault else LanguageConfig.adsLayoutRes
        )
        code = LanguageConfig.codeLanguage

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this@LanguageActivity)
            val itemLanguage = LanguageConfig.items.find { it.code == code }?.let {
                LanguageConfig.items.indexOf(it)
            } ?: run {
                RecyclerView.NO_POSITION
            }
            adapter = LanguageAdapter(
                selected = itemLanguage,
                items = LanguageConfig.items,
                itemLayoutRes = LanguageConfig.itemLayoutRes,
                onClick = { lang ->
                    code = lang.code
                })
        }

        btnNext?.setOnClickListener {
            if (code != "") {
                FOFlowManager.selectLanguage.invoke(code)
                FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (LanguageConfig.nameTracking.isNotBlank()) FOFlowManager.pushTracking.invoke(
            true, LanguageConfig.nameTracking
        )
    }

    override fun onPause() {
        super.onPause()
        if (LanguageConfig.nameTracking.isNotBlank()) FOFlowManager.pushTracking.invoke(
            false, LanguageConfig.nameTracking
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
        val idScreen = "language"
        val isShowOnlyScreen = "isShowOnlyScreen"
    }
}