package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel

data class OnboardingConfig(
    @LayoutRes val onboardingLayoutRes: Int,
    val nameTracking: String,
    val showAdsInter: Boolean,
    val items: List<OnboardingItemModel> = emptyList()
) {
    class Builder {

        private var onboardingLayoutRes: Int = 0

        private var nameTracking: String = ""
        private var showAdsInter: Boolean = false
        private var items: List<OnboardingItemModel> = emptyList()

        fun setOnboardingLayout(@LayoutRes res: Int) = apply { this.onboardingLayoutRes = res }

        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setItems(list: List<OnboardingItemModel>) = apply { this.items = list }

        fun build(): OnboardingConfig {
            require(onboardingLayoutRes != 0) { "onboardingLayoutRes must be set" }

            return OnboardingConfig(
                onboardingLayoutRes = onboardingLayoutRes,
                nameTracking = nameTracking,
                showAdsInter = showAdsInter,
                items = items
            )
        }
    }
}
