package volio.b1.foflow.ui.language

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.adapter.LanguageAdapter
import volio.b1.foflow.config.LanguageConfig
import volio.b1.foflow.R

class LanguageActivity : AppCompatActivity() {
    var code = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_language)
        hideNavigationBar()


        val adContainer = findViewById<FrameLayout>(R.id.layoutAds)
        val recyclerView = findViewById<RecyclerView>(R.id.rvLanguage)

        FOFlowManager.callback?.showNativeAds(
            FOFlowManager.config.language.nameSpaceAds,
            adContainer,
            if (FOFlowManager.isShowDefaultAds(idScreen)) R.layout.native_ads_default else FOFlowManager.config.language.adsLayoutRes,
            FOFlowManager.config.language.nameTracking
        )
        code = FOFlowManager.config.language.codeLanguage

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(this@LanguageActivity)
            val newList = FOFlowManager.config.language.items.toMutableList()
            val itemLanguage = FOFlowManager.config.language.items.find { it.code == code }?.let {
                newList.remove(it)
                newList.add(0, it)
                0
            } ?: run {
                RecyclerView.NO_POSITION
            }
            adapter = LanguageAdapter(
                selected = itemLanguage,
                items = newList,
                onClick = { lang ->
                    code = lang.code
                })
        }

        initListener()
    }

    fun initListener() {
        val isShowOnlyScreen = intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false
        val btnNext = findViewById<TextView>(R.id.tvSelect)
        val imgBack = findViewById<ImageView>(R.id.imgBack)

        if (isShowOnlyScreen) {
            imgBack?.visibility = View.VISIBLE
        } else {
            imgBack?.visibility = View.GONE
        }

        btnNext?.setOnClickListener {
            if (code != "") {
                FOFlowManager.config = FOFlowManager.config.copy(
                    language = FOFlowManager.config.language.copy(codeLanguage = code)
                )
                FOFlowManager.callback?.selectLanguage(code)
                FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
                finish()
            }
        }

        imgBack?.setOnClickListener {
            finish()
        }
        this.onBackPressedDispatcher.addCallback(this, true) {
            if (isShowOnlyScreen) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (FOFlowManager.config.language.nameTracking.isNotBlank()) FOFlowManager.callback?.pushTracking(
            true, FOFlowManager.config.language.nameTracking
        )
    }

    override fun onPause() {
        super.onPause()
        if (FOFlowManager.config.language.nameTracking.isNotBlank()) FOFlowManager.callback?.pushTracking(
            false, FOFlowManager.config.language.nameTracking
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