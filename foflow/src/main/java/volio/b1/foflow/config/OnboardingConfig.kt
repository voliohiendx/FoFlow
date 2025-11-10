package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel

data class OnboardingConfig(
    @LayoutRes val adsLayoutRes: Int,
    @LayoutRes val adsLayoutResFull: Int,
    val nameSpaceAds: String,
    val nameSpaceAdsFull: String,
    val nameTracking: String,
    val showAdsInter: Boolean,
    val items: List<OnboardingItemModel> = listOf(),
){
    class Builder {
        private var adsLayoutRes: Int = 0
        private var adsLayoutResFull: Int = 0
        private var nameSpaceAds: String = ""
        private var nameSpaceAdsFull: String = ""
        private var nameTracking: String = ""
        private var showAdsInter: Boolean = false
        private var items: List<OnboardingItemModel> = listOf()

        fun setAdsLayoutRes(@LayoutRes res: Int) = apply { this.adsLayoutRes = res }
        fun setAdsLayoutResFull(@LayoutRes res: Int) = apply { this.adsLayoutResFull = res }
        fun setNameSpaceAds(name: String) = apply { this.nameSpaceAds = name }
        fun setNameSpaceAdsFull(name: String) = apply { this.nameSpaceAdsFull = name }
        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setItems(items: List<OnboardingItemModel>) = apply { this.items = items }

        fun build() = OnboardingConfig(
            adsLayoutRes, adsLayoutResFull, nameSpaceAds,
            nameSpaceAdsFull, nameTracking, showAdsInter, items
        )
    }
}

