package volio.b1.foflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import volio.b1.foflow.config.FoFlowConfig
import volio.b1.foflow.config.LanguageConfig
import volio.b1.foflow.config.OnboardingConfig
import volio.b1.foflow.model.LanguageItemModel
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.utils.FOFlowCallback
import volio.b1.foflow.R
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
            config = FoFlowConfig(
                language = LanguageConfig(
                    adsLayoutRes = R.layout.native_ads_default,
                    nameSpaceAds = "ADMOB_Native_Language",
                    nameTracking = "language_tracking",
                    codeLanguage = "en",
                    items = listOf(
                        LanguageItemModel(
                            code = "vi",
                            resFlagLanguage = R.drawable.flag_england_demo,
                            nameLanguage = "Vietnamese"
                        ),
                        LanguageItemModel(
                            code = "de",
                            resFlagLanguage = R.drawable.flag_england_demo,
                            nameLanguage = "Brazilian"
                        ),
                        LanguageItemModel(
                            code = "en",
                            resFlagLanguage = R.drawable.flag_england_demo,
                            nameLanguage = "Englis"
                        ),
                    )
                ),
                onboarding = OnboardingConfig(
                    adsLayoutRes = R.layout.native_ads_default,
                    adsLayoutResFull = R.layout.native_ads_default,
                    nameSpaceAds = "ADMOB_Native_Language",
                    nameSpaceAdsFull = "ADMOB_Native_Language",
                    nameTracking = "onboarding_tracking",
                    items = listOf(
                        OnboardingItemModel(
                            title = R.string.img_onboarding_1,
                            content = null,
                            pathImage = "file:///android_asset/onboarding/img_onboarding_1.jpg",
                            adsVisibility = View.VISIBLE,
                        ), OnboardingItemModel(
                            title = R.string.img_onboarding_2,
                            content = null,
                            pathImage = "file:///android_asset/onboarding/img_onboarding_2.jpg",
                            adsVisibility = View.GONE,
                        ), OnboardingItemModel(
                            title = R.string.img_onboarding_3,
                            content = null,
                            pathImage = "file:///android_asset/onboarding/img_onboarding_3.jpg",
                            adsVisibility = View.VISIBLE,
                        )
                    )
                )
            ),
            callback = object : FOFlowCallback {
                override fun showNativeAds(
                    spaceName: String,
                    viewGroup: ViewGroup,
                    idLayoutAds: Int,
                    screenName: String
                ) {

                }

                override fun pushTracking(isResume: Boolean, screenName: String) {

                }

                override fun selectLanguage(codeLanguage: String) {

                }

            },
            context = this,
            pathAsset =""
        )

        FOFlowManager.startFOFlow(
            this,
            intentWhenFinish = Intent(this, MainActivity::class.java),
            finishFOFlow = {

            })

//        FOFlowManager.showOnlyScreen(
//            this,
//            "welcome",
//            Intent(this, MainActivity::class.java)
//        )
    }
}