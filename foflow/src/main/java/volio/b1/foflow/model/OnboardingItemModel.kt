package volio.b1.foflow.model

data class OnboardingItemModel(
    val title: Int,
    val content: Int? = null,
    val pathImage: String,
    val adsVisibility: Int
)