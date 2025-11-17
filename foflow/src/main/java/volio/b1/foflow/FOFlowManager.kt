package volio.b1.foflow

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.LayoutRes
import volio.b1.foflow.model.FlowModel
import volio.b1.foflow.ui.language.LanguageActivity
import volio.b1.foflow.ui.onboarding.OnboardingActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import volio.b1.foflow.config.FoFlowConfig
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.utils.FOFlowCallback
import java.io.InputStream

object FOFlowManager {
    private var finishFOFlow: () -> Unit = {}
    private var intentWhenFinish: Intent? = null
    internal lateinit var config: FoFlowConfig

    var callback: FOFlowCallback? = null

    var isInitDataConfig = false

    private val flowData = mutableListOf<FlowModel>().apply {
        add(FlowModel("language", isShowAdsDefault = true))
        add(FlowModel("onboarding", isShowAdsDefault = false))
    }

    fun init(
        context: Context,
        pathAsset: String,
        callback: FOFlowCallback
    ) {
        this.callback = callback
        getStringAssetFile(context, pathAsset)?.let {
            initDataRemote(it)
        }
    }

    fun initDataConfig(config: FoFlowConfig) {
        this.config = config
        isInitDataConfig = true
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
        finishFOFlow: () -> Unit
    ) {
        if (isInitDataConfig) {
            goNextScreen(context, "", false)
            this.intentWhenFinish = intentWhenFinish
            this.finishFOFlow = finishFOFlow
        } else {
            throw IllegalStateException("FOFlowManager chưa được khởi tạo. Hãy gọi FOFlowManager.initDataConfig() trước khi startFOFlow().")
        }
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
        if (isInitDataConfig) {
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
        } else throw IllegalStateException("FOFlowManager chưa được khởi tạo. Hãy gọi FOFlowManager.initDataConfig() trước khi startFOFlow().")
    }

    fun setLanguageLayout(@LayoutRes languageLayout: Int) {
        config = config.copy(
            language = config.language.copy(
                languageLayoutRes = languageLayout
            )
        )
    }

    fun setItemLanguageLayout(@LayoutRes itemLanguageLayout: Int) {
        config = config.copy(
            language = config.language.copy(
                itemLanguageLayoutRes = itemLanguageLayout
            )
        )
    }

    fun setLanguageLayoutAds(list: List<Pair<Int, String>>) {
        config = config.copy(
            language = config.language.copy(
                adsLanguage = list
            )
        )
    }

    fun setLanguageLayoutAdsReload(list: List<Pair<Int, String>>) {
        config = config.copy(
            language = config.language.copy(
                adsReload = list
            )
        )
    }

    fun setOnboardingLayoutAds(list: List<Pair<Int, String>>) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                adsOnboarding = list
            )
        )
    }

    fun setOnboardingLayout(@LayoutRes onboardingLayout: Int) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                onboardingLayoutRes = onboardingLayout
            )
        )
    }

    fun setItemOnboardingLayout(@LayoutRes itemOnboardingLayout: Int) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                itemOnboarding = itemOnboardingLayout
            )
        )
    }

    fun setItemAdsFullOnboarding(@LayoutRes itemAdsFullOnboarding: Int) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                itemAdsFullOnboarding = itemAdsFullOnboarding
            )
        )
    }

    fun setOnboardingLayoutAdsFull(
        list: List<Pair<Int, String>>
    ) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                adsOnboardingFull = list,
            )
        )
    }

    fun setDataOnboardingItem(items: List<OnboardingItemModel>) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                items = items
            )
        )
    }

    fun setShowInterAdsOnboarding(isShow: Boolean) {
        config = config.copy(
            onboarding = config.onboarding.copy(
                showAdsInter = isShow
            )
        )
    }

    fun setShowInterAdsLanguage(isShow: Boolean) {
        config = config.copy(
            language = config.language.copy(
                showAdsInter = isShow
            )
        )
    }

    fun isShowDefaultAds(idScreen: String): Boolean {
        return flowData.find { it.id == idScreen }?.isShowAdsDefault ?: true
    }

    fun isEnableShowAds(spaceName: String): Boolean {
        return callback?.isEnableShowAds(spaceName) ?: false
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