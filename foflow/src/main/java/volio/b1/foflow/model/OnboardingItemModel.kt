package volio.b1.foflow.model

data class OnboardingItemModel(
    val title: Int,
    val content: Int? = null,
    val pathImage: String,
    val adsVisibility: Int,
    val type: Int = TYPE_NORMAL,
    val layoutAds: Int = 0,
    val layoutItem: Int = 0,
    val spaceAds: String = "",
    val timeDelayNextScreenAdsFull: Long = 0
) {
    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_ADS = 1
    }
}