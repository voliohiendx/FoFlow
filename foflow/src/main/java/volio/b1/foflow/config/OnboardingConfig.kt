package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.R

object OnboardingConfig {

    var adsLayoutRes: Int = R.layout.native_ads_default
    var nameSpaceAds: String = ""
    var nameTracking: String = ""
    var items: List<OnboardingItemModel> = listOf()

    fun initData(
        @LayoutRes adsLayoutRes: Int,
        nameSpaceAds: String,
        nameTracking : String,
        items: List<OnboardingItemModel>
    ) {
        this.adsLayoutRes = adsLayoutRes
        this.nameSpaceAds = nameSpaceAds
        this.nameTracking = nameTracking
        this.items = items
    }
}