package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel

data class OnboardingConfig(
    @LayoutRes val onboardingLayoutRes: Int,
    @LayoutRes val itemOnboarding: Int,
    @LayoutRes val itemAdsFullOnboarding: Int,

    // List Ads (Normal + Namespace)
    val adsOnboarding: List<Pair<Int, String>>,

    // List Ads Full + Namespace
    val adsOnboardingFull: List<Pair<Int, String>>,

    val nameTracking: String,
    val showAdsInter: Boolean,
    val items: List<OnboardingItemModel> = emptyList()
) {

    class Builder {

        private var onboardingLayoutRes: Int = 0
        private var itemOnboarding: Int = 0
        private var itemAdsFullOnboarding: Int = 0

        private var ads: List<Pair<Int, String>> = emptyList()
        private var adsFull: List<Pair<Int, String>> = emptyList()

        private var nameTracking: String = ""
        private var showAdsInter: Boolean = false
        private var items: List<OnboardingItemModel> = emptyList()

        fun setOnboardingLayout(@LayoutRes res: Int) = apply { this.onboardingLayoutRes = res }
        fun setItemOnboarding(@LayoutRes res: Int) = apply { this.itemOnboarding = res }
        fun setItemAdsFullOnboarding(@LayoutRes res: Int) = apply { this.itemAdsFullOnboarding = res }

        fun setAdsOnboarding(list: List<Pair< Int, String>>) = apply { this.ads = list }
        fun setAdsOnboardingFull(list: List<Pair< Int, String>>) = apply { this.adsFull = list }

        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setItems(list: List<OnboardingItemModel>) = apply { this.items = list }

        fun build(): OnboardingConfig {
            require(onboardingLayoutRes != 0) { "onboardingLayoutRes must be set" }
            require(itemOnboarding != 0) { "itemOnboarding must be set" }
            require(itemAdsFullOnboarding != 0) { "itemAdsFullOnboarding must be set" }

            return OnboardingConfig(
                onboardingLayoutRes = onboardingLayoutRes,
                itemOnboarding = itemOnboarding,
                itemAdsFullOnboarding = itemAdsFullOnboarding,
                adsOnboarding = ads,
                adsOnboardingFull = adsFull,
                nameTracking = nameTracking,
                showAdsInter = showAdsInter,
                items = items
            )
        }
    }
}
