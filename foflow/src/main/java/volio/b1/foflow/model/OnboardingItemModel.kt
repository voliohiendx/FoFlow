package volio.b1.foflow.model

data class OnboardingItemModel(
    val title: Int,
    val content: Int? = null,
    val pathImage: String,
    val type: Int = TYPE_NORMAL,
    val layoutItem: Int = 0,
    var adsData: AdsOnboardingModel? = null,
) {
    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_ADS = 1
    }
}