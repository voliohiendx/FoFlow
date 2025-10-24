package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.OnboardingItemModel

data class OnboardingConfig(
    @LayoutRes val adsLayoutRes: Int,
    @LayoutRes val adsLayoutResFull: Int,
    val nameSpaceAds: String,
    val nameSpaceAdsFull: String,
    val nameTracking: String,
    val items: List<OnboardingItemModel> = listOf()
)
