package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel

data class OnboardingConfig(
    @LayoutRes val adsLayoutRes: Int,
    @LayoutRes val adsLayoutResFull: List<Int>,
    val nameSpaceAds: String,
    val nameSpaceAdsFull: List<String>,
    val nameTracking: String,
    val showAdsInter: Boolean,
    val items: List<OnboardingItemModel> = listOf()
) {
    class Builder {
        private var adsLayoutRes: Int = 0
        private var adsLayoutResFull: List<Int> = emptyList()
        private var nameSpaceAds: String = ""
        private var nameSpaceAdsFull: List<String> = emptyList()
        private var nameTracking: String = ""
        private var showAdsInter: Boolean = false
        private var items: List<OnboardingItemModel> = emptyList()

        fun setAdsLayoutRes(@LayoutRes res: Int) = apply { this.adsLayoutRes = res }
        fun setAdsLayoutResFull(@LayoutRes res: List<Int>) = apply { this.adsLayoutResFull = res }
        fun setNameSpaceAds(name: String) = apply { this.nameSpaceAds = name }
        fun setNameSpaceAdsFull(list: List<String>) = apply { this.nameSpaceAdsFull = list }
        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setItems(items: List<OnboardingItemModel>) = apply { this.items = items }

        fun build() = OnboardingConfig(
            adsLayoutRes = adsLayoutRes,
            adsLayoutResFull = adsLayoutResFull,
            nameSpaceAds = nameSpaceAds,
            nameSpaceAdsFull = nameSpaceAdsFull,
            nameTracking = nameTracking,
            showAdsInter = showAdsInter,
            items = items
        )
    }
}

