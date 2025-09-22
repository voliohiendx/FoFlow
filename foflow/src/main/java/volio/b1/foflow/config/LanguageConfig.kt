package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.LanguageItemModel

data class LanguageConfig(
    @LayoutRes val adsLayoutRes: Int,
    val nameSpaceAds: String,
    val nameTracking: String,
    val codeLanguage: String,
    val items: List<LanguageItemModel> = listOf()
)
