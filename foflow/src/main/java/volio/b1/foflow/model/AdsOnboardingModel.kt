package volio.b1.foflow.model

data class AdsOnboardingModel(
    val layoutAds: Int = 0,
    val spaceAds: String = "",
    val adsVisibility: Int,
    val timeDelayNextScreenAdsFull: Long = 0
)
