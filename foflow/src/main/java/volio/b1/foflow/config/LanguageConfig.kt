package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.LanguageItemModel
import volio.b1.foflow.R

object LanguageConfig {
    var adsLayoutRes: Int = R.layout.native_ads_default
    var nameSpaceAds: String = ""

    var nameTracking: String = ""

    var codeLanguage = "en"
    var items: List<LanguageItemModel> = listOf()

    fun initData(
        @LayoutRes adsLayoutRes: Int,
        nameSpaceAds: String,
        nameTracking: String,
        codeLanguage: String,
        items: List<LanguageItemModel>
    ) {
        this.adsLayoutRes = adsLayoutRes
        this.nameSpaceAds = nameSpaceAds
        this.nameTracking = nameTracking
        this.codeLanguage = codeLanguage
        this.items = items
    }
}