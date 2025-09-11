package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel
import volio.b1.foflow.R

object OnboardingConfig {
    var activityLayoutRes: Int = R.layout.activity_onboarding_default
    var itemLayoutRes: Int = R.layout.item_onboarding_default

    var adsLayoutRes: Int = R.layout.native_ads_default
    var adsLayoutResDefault: Int = R.layout.native_ads_default

    var nameSpaceAds: String = ""
    var nameTracking: String = ""
    var items: List<OnboardingItemModel> = listOf()

    fun initData(
        @LayoutRes activityLayoutRes: Int,
        @LayoutRes itemLayoutRes: Int,
        @LayoutRes adsLayoutRes: Int,
        @LayoutRes adsLayoutResDefault: Int,
        nameSpaceAds: String,
        nameTracking : String,
        items: List<OnboardingItemModel>
    ) {
        this.activityLayoutRes = activityLayoutRes
        this.itemLayoutRes = itemLayoutRes
        this.adsLayoutRes = adsLayoutRes
        this.nameSpaceAds = nameSpaceAds
        this.nameTracking = nameTracking
        this.items = items
        this.adsLayoutResDefault = adsLayoutResDefault
    }
}