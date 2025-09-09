package volio.b1.foflow

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import volio.b1.foflow.model.FlowModel
import volio.b1.foflow.ui.language.LanguageActivity
import volio.b1.foflow.config.LanguageConfig
import volio.b1.foflow.model.LanguageItemModel
import volio.b1.foflow.ui.onboarding.OnboardingActivity
import volio.b1.foflow.config.OnboardingConfig
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.ui.welcome.WelcomeActivity
import volio.b1.foflow.config.WelcomeConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FOFlowManager {
    private var finishFOFlow: () -> Unit = {}
    private var intentWhenFinish: Intent? = null

    var showNativeAds: (
        spaceName: String, viewGroup: ViewGroup, idLayoutAds: Int
    ) -> Unit = { _, _, _ -> }

    var pushTracking: (
        isResume: Boolean, screenName: String
    ) -> Unit = { _, _ -> }

    val codeLanguage: String
        get() = LanguageConfig.codeLanguage

    private val flowData = mutableListOf<FlowModel>().apply {
        add(FlowModel("language", isShowAdsDefault = true))
        add(FlowModel("onboarding", isShowAdsDefault = false))
        add(FlowModel("welcome", isShowAdsDefault = false))
    }

    fun initDataConfig(jsonConfig: String) {
        val listType = object : TypeToken<List<FlowModel>>() {}.type
        val configList: List<FlowModel> = Gson().fromJson(jsonConfig, listType)
        flowData.clear()
        flowData.addAll(configList)
    }

    fun initLanguageData(
        @LayoutRes activityLayoutRes: Int,
        @LayoutRes itemLayoutRes: Int,
        @LayoutRes adsLayoutRes: Int,
        @LayoutRes adsLayoutResDefault: Int,
        nameSpaceAds: String,
        nameTracking: String,
        codeLanguage: String,
        items: List<LanguageItemModel>
    ) {
        LanguageConfig.initData(
            activityLayoutRes = activityLayoutRes,
            itemLayoutRes = itemLayoutRes,
            adsLayoutRes = adsLayoutRes,
            nameSpaceAds = nameSpaceAds,
            adsLayoutResDefault = adsLayoutResDefault,
            nameTracking = nameTracking,
            codeLanguage = codeLanguage,
            items = items
        )
    }

    fun initOnboardingData(
        @LayoutRes activityLayoutRes: Int,
        @LayoutRes itemLayoutRes: Int,
        @LayoutRes adsLayoutRes: Int,
        @LayoutRes adsLayoutResDefault: Int,
        nameSpaceAds: String,
        nameTracking: String,
        items: List<OnboardingItemModel>
    ) {
        OnboardingConfig.initData(
            activityLayoutRes = activityLayoutRes,
            itemLayoutRes = itemLayoutRes,
            adsLayoutRes = adsLayoutRes,
            nameSpaceAds = nameSpaceAds,
            adsLayoutResDefault = adsLayoutResDefault,
            nameTracking = nameTracking,
            items = items
        )
    }

    fun initWelcomeData(
        @LayoutRes activityLayoutRes: Int,
        @LayoutRes adsLayoutRes: Int,
        @LayoutRes adsLayoutResDefault: Int,
        nameSpaceAds: String,
        nameTracking: String,
    ) {
        WelcomeConfig.initData(
            activityLayoutRes = activityLayoutRes,
            adsLayoutRes = adsLayoutRes,
            adsLayoutResDefault = adsLayoutResDefault,
            nameSpaceAds = nameSpaceAds,
            nameTracking = nameTracking
        )
    }

    fun startFOFlow(
        context: Context,
        intentWhenFinish: Intent?,
        finishFOFlow: () -> Unit, showNativeAds: (
            spaceName: String, viewGroup: ViewGroup, idLayoutAds: Int
        ) -> Unit, pushTracking: (
            isResume: Boolean, screenName: String
        ) -> Unit
    ) {
        goNextScreen(context, "", false)
        this.intentWhenFinish = intentWhenFinish
        this.finishFOFlow = finishFOFlow
        this.showNativeAds = showNativeAds
        this.pushTracking = pushTracking
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

                WelcomeActivity.idScreen -> {
                    context.startActivity(Intent(context, WelcomeActivity::class.java).apply {
                        putExtra(WelcomeActivity.isShowOnlyScreen, isShowOnlyScreen)
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
        finishFOFlow: () -> Unit
    ) {
        this.finishFOFlow = finishFOFlow
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

            WelcomeActivity.idScreen -> {
                context.startActivity(Intent(context, WelcomeActivity::class.java).apply {
                    putExtra(WelcomeActivity.isShowOnlyScreen, true)
                })
                return
            }
        }
    }

    fun isShowDefaultAds(idScreen: String): Boolean {
        return flowData.find { it.id == idScreen }?.isShowAdsDefault ?: true
    }
}