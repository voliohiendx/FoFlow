package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.R

object WelcomeConfig {
    var activityLayoutRes: Int = R.layout.activity_welcome_default

    var adsLayoutRes: Int = R.layout.native_ads_default
    var adsLayoutResDefault: Int = R.layout.native_ads_default

    var nameSpaceAds: String = ""

    var nameTracking: String = ""
    fun initData(
        @LayoutRes activityLayoutRes: Int,
        @LayoutRes adsLayoutRes: Int,
        @LayoutRes adsLayoutResDefault: Int,
        nameSpaceAds: String,
        nameTracking : String,
    ) {
        this.activityLayoutRes = activityLayoutRes
        this.adsLayoutRes = adsLayoutRes
        this.nameSpaceAds = nameSpaceAds
        this.nameTracking = nameTracking
        this.adsLayoutResDefault = adsLayoutResDefault
    }
}