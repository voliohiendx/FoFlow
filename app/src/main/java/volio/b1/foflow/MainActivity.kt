package volio.b1.foflow

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import c.b1.fo.FOFlowManager
import volio.b1.foflow.model.LanguageItemModel
import volio.b1.foflow.model.OnboardingItemModel

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
        FOFlowManager.initLanguageData(
            activityLayoutRes = R.layout.activity_language_default,
            itemLayoutRes = R.layout.item_language_default,
            adsLayoutRes = R.layout.native_ads_default,
            adsLayoutResDefault = R.layout.native_ads_default,
            nameSpaceAds = "ADMOB_Native_Language",
            nameTracking = "language_tracking",
            codeLanguage = "en",
            items = mutableListOf(
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
        )

        FOFlowManager.initOnboardingData(
            activityLayoutRes = R.layout.activity_onboarding_default,
            itemLayoutRes = R.layout.item_onboarding_default,
            adsLayoutRes = R.layout.native_ads_default,
            adsLayoutResDefault = R.layout.native_ads_default,
            nameSpaceAds = "ADMOB_Native_Language",
            nameTracking = "onboarding_tracking",
            items = listOf(
                OnboardingItemModel(
                    title = "img_onboarding_1",
                    content = "",
                    pathImage = "file:///android_asset/onboarding/img_onboarding_1.jpg",
                    isShowAds = true,
                ), OnboardingItemModel(
                    title = "img_onboarding_2",
                    content = "",
                    pathImage = "file:///android_asset/onboarding/img_onboarding_2.jpg",
                    isShowAds = false,
                ), OnboardingItemModel(
                    title = "img_onboarding_3",
                    content = "",
                    pathImage = "file:///android_asset/onboarding/img_onboarding_3.jpg",
                    isShowAds = true,
                )
            )
        )

        FOFlowManager.initWelcomeData(
            activityLayoutRes = R.layout.activity_welcome_default,
            adsLayoutRes = R.layout.native_ads_default,
            adsLayoutResDefault = R.layout.native_ads_default,
            nameSpaceAds = "ADMOB_Native_Language",
            nameTracking = "onboarding_tracking",
        )

        FOFlowManager.startFOFlow(
            this,
            intentWhenFinish = Intent(this, MainActivity::class.java),
            finishFOFlow = {

            },
            showNativeAds = { spaceName, viewGroup, adsLayoutRes ->
//                AdsUtils.showAdsNative(
//                    spaceName = spaceName,
//                    viewGroup = viewGroup,
//                    idLayoutAds = adsLayoutRes,
//                    screenName(),
//                )
            },
            pushTracking = { isResume, screenName ->
//                if (isResume) {
//                    Tracking.onScreenResume(screenName)
//                } else {
//                    Tracking.onScreenPause(screenName)
//                }
            },
            selectLanguage = {
//                kotlin.runCatching {
//                    MultiLanguages.setAppLanguage(context, Locale(it))
//                }.onFailure {
//                    it.printStackTrace()
//                }
            })

//        FOFlowManager.showOnlyScreen(
//            this,
//            "welcome",
//            Intent(this, MainActivity::class.java)
//        )
    }
}