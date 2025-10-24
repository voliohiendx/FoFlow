package volio.b1.foflow.model

data class OnboardingItemModel(
    val title: String,
    val content: String,
    val pathImage: String,
    val adsVisibility: Int,
    val type: Int = TYPE_NORMAL
){
    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_ADS = 1
    }
}