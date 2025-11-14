package volio.b1.foflow.ui.language

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import volio.b1.foflow.FOFlowManager
import volio.b1.foflow.adapter.LanguageAdapter
import volio.b1.foflow.R

class LanguageActivity : AppCompatActivity() {
    var code = ""

    private var adContainer: FrameLayout? = null
    private var adContainerMore: FrameLayout? = null
    private var recyclerView: RecyclerView? = null
    private var imvSelect: ImageView? = null
    private var tvSelect: TextView? = null
    private var imgBack: ImageView? = null
    private var viewApplyLanguage: View? = null

    private var lastClickTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(FOFlowManager.config.language.languageLayoutRes)
        hideNavigationBar()
        initView()
        initRecyclerview()
        initListener()
        handlerShowAds()
    }

    fun initView() {
        code = FOFlowManager.config.language.codeLanguage
        adContainer = findViewById<FrameLayout>(R.id.layoutAds)
        adContainerMore = findViewById<FrameLayout>(R.id.layoutAdsMore)
        recyclerView = findViewById<RecyclerView>(R.id.rvLanguage)
        tvSelect = findViewById<TextView>(R.id.tvSelect)
        imvSelect = findViewById<ImageView>(R.id.imvSelect)
        imgBack = findViewById<ImageView>(R.id.imgBack)
        viewApplyLanguage = findViewById<ConstraintLayout>(R.id.clApplyLanguage)

        setSelectAlpha(code)
    }

    fun initListener() {
        val isShowOnlyScreen = intent?.getBooleanExtra(isShowOnlyScreen, false) ?: false

        if (isShowOnlyScreen) {
            imgBack?.visibility = View.VISIBLE
        } else {
            imgBack?.visibility = View.GONE
        }

        imvSelect?.setOnClickListener {
            onClickNext(isShowOnlyScreen)
        }
        tvSelect?.setOnClickListener {
            onClickNext(isShowOnlyScreen)
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

    fun initRecyclerview() {
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
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime >= 2000) {
                        lastClickTime = currentTime
                        handlerShowAdsReload()
                    }
                    setSelectAlpha(code)
                })
        }
    }

    fun handlerShowAds() {
        FOFlowManager.config.language.adsLanguage.forEachIndexed { index, (layoutRes, spaceName) ->
            when (index) {
                0 -> adContainer?.let { container ->
                    FOFlowManager.callback?.showNativeAds(
                        spaceName,
                        container,
                        layoutRes,
                        FOFlowManager.config.language.nameTracking
                    )
                }

                1 -> adContainerMore?.let { container ->
                    FOFlowManager.callback?.showNativeAds(
                        spaceName,
                        container,
                        layoutRes,
                        FOFlowManager.config.language.nameTracking
                    )
                }
            }
        }
    }

    fun handlerShowAdsReload() {
        FOFlowManager.config.language.adsReload.forEachIndexed { index, (layoutRes, spaceName) ->
            when (index) {
                0 -> adContainer?.let { container ->
                    FOFlowManager.callback?.showNativeAds(
                        spaceName,
                        container,
                        layoutRes,
                        FOFlowManager.config.language.nameTracking
                    )
                }

                1 -> adContainerMore?.let { container ->
                    FOFlowManager.callback?.showNativeAds(
                        spaceName,
                        container,
                        layoutRes,
                        FOFlowManager.config.language.nameTracking
                    )
                }
            }
        }
    }

    fun onClickNext(isShowOnlyScreen: Boolean) {
        if (code != "") {
            fun setLanguage() {
                FOFlowManager.config = FOFlowManager.config.copy(
                    language = FOFlowManager.config.language.copy(codeLanguage = code)
                )
                FOFlowManager.callback?.selectLanguage(code)
                if (FOFlowManager.config.language.showAdsInter) {
                    FOFlowManager.callback?.showInterAds(this.lifecycle) {
                        FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
                        finish()
                    }
                } else {
                    FOFlowManager.goNextScreen(this, idScreen, isShowOnlyScreen)
                    finish()
                }
            }
            if (FOFlowManager.config.language.showUiApply && viewApplyLanguage != null) {
                viewApplyLanguage?.visibility = View.VISIBLE

                viewApplyLanguage?.postDelayed({
                   // viewApplyLanguage?.visibility = View.GONE
                    setLanguage()
                }, 2000)
            } else {
                setLanguage()
            }
        } else {
            Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setSelectAlpha(code: String) {
        val alphaValue = if (code.isEmpty()) 0.3f else 1f
        imvSelect?.alpha = alphaValue
        tvSelect?.alpha = alphaValue
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