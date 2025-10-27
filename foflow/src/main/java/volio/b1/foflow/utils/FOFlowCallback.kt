package volio.b1.foflow.utils

import android.view.ViewGroup

interface FOFlowCallback {
    fun showNativeAds(spaceName: String, viewGroup: ViewGroup, idLayoutAds: Int, screenName: String)
    fun pushTracking(isResume: Boolean, screenName: String)
    fun selectLanguage(codeLanguage: String)
    fun showInterAds(onNextScreen: () -> Unit, )
}