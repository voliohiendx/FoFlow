package volio.b1.foflow.utils

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import volio.b1.foflow.model.OnboardingItemModel

interface FOFlowCallback {
    fun showNativeAds(spaceName: String, viewGroup: ViewGroup, idLayoutAds: Int, screenName: String)
    fun pushTracking(isResume: Boolean, screenName: String)
    fun selectLanguage(codeLanguage: String)
    fun showInterAds(lifecycle: Lifecycle, onNextScreen: () -> Unit)
    fun isEnableShowAds(spaceName: String): Boolean

    fun showApplyLanguage(isShow: Boolean, view: View) {}

    fun scrollPageOnboarding(position: Int, onboardingItemModel: OnboardingItemModel) {}
}