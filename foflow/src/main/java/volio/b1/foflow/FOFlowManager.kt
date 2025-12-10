package volio.b1.foflow

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import volio.b1.foflow.model.FlowModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import volio.b1.foflow.config.FoFlowConfig
import volio.b1.foflow.ui.language.LanguageActivity
import volio.b1.foflow.ui.onboarding.OnboardingActivity
import java.io.InputStream

object FOFlowManager {
    private var finishFOFlow: () -> Unit = {}
    private var intentWhenFinish: Intent? = null
    var config: FoFlowConfig? = null

    private val flowData = mutableListOf<FlowModel>().apply {
        add(FlowModel("language"))
        add(FlowModel("onboarding"))
    }

    fun init(
        context: Context,
        pathAsset: String,
        config: FoFlowConfig
    ) {
        getStringAssetFile(context, pathAsset)?.let {
            initDataRemote(it)
        }
        this.config = config
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
        fOFlowFinish: () -> Unit
    ) {
        config?.let {
            goNextScreen(context, "", false)
            this.intentWhenFinish = intentWhenFinish
            this.finishFOFlow = fOFlowFinish
        }
    }

    fun setLanguageFragment(language: Fragment) {
        config?.language = language
    }

    fun setOnboardingFragment(onboarding: Fragment) {
        config?.onboarding = onboarding
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
                        putExtra(LanguageActivity.keyShowOnlyScreen, isShowOnlyScreen)
                    })
                    return
                }

                OnboardingActivity.idScreen -> {
                    context.startActivity(Intent(context, OnboardingActivity::class.java).apply {
                        putExtra(OnboardingActivity.keyShowOnlyScreen, isShowOnlyScreen)
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
        config?.let {
            this.intentWhenFinish = intentWhenFinish
            when (idScreen) {
                LanguageActivity.idScreen -> {
                    context.startActivity(Intent(context, LanguageActivity::class.java).apply {
                        putExtra(LanguageActivity.keyShowOnlyScreen, true)
                    })
                    return
                }

                OnboardingActivity.idScreen -> {
                    context.startActivity(Intent(context, OnboardingActivity::class.java).apply {
                        putExtra(OnboardingActivity.keyShowOnlyScreen, true)
                    })
                    return
                }
            }
        }
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