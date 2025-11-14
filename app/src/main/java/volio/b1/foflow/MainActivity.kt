package volio.b1.foflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import volio.b1.foflow.config.FoFlowConfig
import volio.b1.foflow.config.LanguageConfig
import volio.b1.foflow.config.OnboardingConfig
import volio.b1.foflow.model.LanguageItemModel
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.utils.FOFlowCallback
import volio.b1.foflow.R
import volio.b1.foflow.model.OnboardingItemModel.Companion.TYPE_ADS

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        demo()
    }

    fun demo() {
        FOFlowManager.init(
            context = this,
            pathAsset = "",
            callback = object : FOFlowCallback {
                override fun showNativeAds(
                    spaceName: String, viewGroup: ViewGroup, idLayoutAds: Int, screenName: String
                ) {

                }

                override fun pushTracking(isResume: Boolean, screenName: String) {

                }

                override fun selectLanguage(codeLanguage: String) {

                }

                override fun showInterAds(
                    lifecycle: Lifecycle, onNextScreen: () -> Unit
                ) {

                }


                override fun isEnableShowAds(spaceName: String): Boolean {
                    return true
                }
            },
        )
        FOFlowManager.initDataConfig(
            config = FoFlowConfig.Builder().language(
                LanguageConfig.Builder().setAdsLayoutRes(listOf(R.layout.native_ads_default))
                    .setNameSpaceAds(listOf("")).setNameTracking("").setCodeLanguage("")
                    .setShowAdsInter(false).setItems(listOf()).build()
            ).onboarding(
                OnboardingConfig.Builder().setAdsLayoutRes(R.layout.native_ads_default)
                    .setAdsLayoutResFull(listOf(R.layout.native_ads_full_default))
                    .setNameSpaceAds("")
                    .setNameSpaceAdsFull(listOf("")).setNameTracking("").setShowAdsInter(false)
                    .setItems(listOf()).build()
            ).build()
        )

        FOFlowManager.startFOFlow(
            this, intentWhenFinish = Intent(this, MainActivity::class.java), finishFOFlow = {

            })

    }

}