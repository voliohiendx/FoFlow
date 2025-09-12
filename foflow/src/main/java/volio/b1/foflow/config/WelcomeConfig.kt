package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.R

object WelcomeConfig {

    var adsLayoutRes: Int = R.layout.native_ads_default
    var nameSpaceAds: String = ""

    var nameTracking: String = ""
    fun initData(
        @LayoutRes adsLayoutRes: Int,
        nameSpaceAds: String,
        nameTracking : String,
    ) {
        this.adsLayoutRes = adsLayoutRes
        this.nameSpaceAds = nameSpaceAds
        this.nameTracking = nameTracking
    }
}