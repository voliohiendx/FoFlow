package volio.b1.foflow

import android.content.Context
import android.content.Intent
import androidx.annotation.LayoutRes
import volio.b1.foflow.model.FlowModel
import volio.b1.foflow.ui.language.LanguageActivity
import volio.b1.foflow.ui.onboarding.OnboardingActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import volio.b1.foflow.config.FoFlowConfig
import volio.b1.foflow.utils.FOFlowCallback
import java.io.InputStream

object FOFlowManager {
    private var finishFOFlow: () -> Unit = {}
    private var intentWhenFinish: Intent? = null
    internal lateinit var config: FoFlowConfig

    var callback: FOFlowCallback? = null

    private val flowData = mutableListOf<FlowModel>().apply {
        add(FlowModel("language", isShowAdsDefault = true))
        add(FlowModel("onboarding", isShowAdsDefault = false))
    }

    var isEnableShowAdsBySpaceName: (String) -> Boolean = { true }

    fun init(
        context: Context,
        pathAsset: String,
        config: FoFlowConfig,
        callback: FOFlowCallback,
        isEnableShowAdsBySpaceName: (String) -> Boolean
    ) {
        this.config = config
        this.callback = callback
        this.isEnableShowAdsBySpaceName = isEnableShowAdsBySpaceName
        getStringAssetFile(context, pathAsset)?.let {
            initDataRemote(it)
        }
    }

    fun initDataRemote(jsonConfig: String) {
        val listType = object : TypeToken<List<FlowModel>>() {}.type
        val configList: List<FlowModel> = Gson().fromJson(jsonConfig, listType)
        flowData.clear()
        flowData.addAll(configList)
    }

    fun startFOFlow(
        context: Context,
        intentWhenFinish: Intent?,
        finishFOFlow: () -> Unit,

        ) {
        goNextScreen(context, "", false)
        this.intentWhenFinish = intentWhenFinish
        this.finishFOFlow = finishFOFlow
    }

    fun goNextScreen(context: Context, idScreen: String, isShowOnlyScreen: Boolean) {
        val indexNextScreen = flowData.find { it.id == idScreen }?.let {
            flowData.indexOf(it) + 1
        } ?: run {
            0
        }
        val nextScreen = flowData.getOrNull(indexNextScreen)
        if (!isShowOnlyScreen && nextScreen != null) {
            when (nextScreen.id) {
                LanguageActivity.idScreen -> {
                    context.startActivity(Intent(context, LanguageActivity::class.java).apply {
                        putExtra(LanguageActivity.isShowOnlyScreen, isShowOnlyScreen)
                    })
                    return
                }

                OnboardingActivity.idScreen -> {
                    context.startActivity(Intent(context, OnboardingActivity::class.java).apply {
                        putExtra(OnboardingActivity.isShowOnlyScreen, isShowOnlyScreen)
                    })
                    return
                }
            }
        } else {
            finishFOFlow.invoke()
            intentWhenFinish?.let {
                context.startActivity(it)
                intentWhenFinish = null
            }
        }
    }

    fun showOnlyScreen(
        context: Context,
        idScreen: String,
        intentWhenFinish: Intent?,
    ) {
        this.intentWhenFinish = intentWhenFinish
        when (idScreen) {
            LanguageActivity.idScreen -> {
                context.startActivity(Intent(context, LanguageActivity::class.java).apply {
                    putExtra(LanguageActivity.isShowOnlyScreen, true)
                })
                return
            }

            OnboardingActivity.idScreen -> {
                context.startActivity(Intent(context, OnboardingActivity::class.java).apply {
                    putExtra(OnboardingActivity.isShowOnlyScreen, true)
                })
                return
            }
        }
    }

    fun setLanguageLayoutAds(@LayoutRes adsLayoutRes: Int) {
        config = config.copy(
            language = config.language.copy(
                adsLayoutRes = adsLayoutRes
            )
        )
    }

    fun setOnboardingLayoutAds(@LayoutRes adsLayoutRes: Int) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                adsLayoutRes = adsLayoutRes
            )
        )
    }

    fun isShowDefaultAds(idScreen: String): Boolean {
        return flowData.find { it.id == idScreen }?.isShowAdsDefault ?: true
    }

    fun isEnableShowAdsBySpaceName(space: String): Boolean {
        return (isEnableShowAdsBySpaceName.invoke(space))
    }

    private fun getStringAssetFile(context: Context, path: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(path)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            return null
        }
    }
}