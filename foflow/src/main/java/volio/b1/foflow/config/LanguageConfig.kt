package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.LanguageItemModel

data class LanguageConfig(
    @LayoutRes val adsLayoutRes: Int,
    val nameSpaceAds: String,
    val nameTracking: String,
    val codeLanguage: String,
    val showAdsInter: Boolean,
    val items: List<LanguageItemModel> = listOf()
){
    class Builder {
        private var adsLayoutRes: Int = 0
        private var nameSpaceAds: String = ""
        private var nameTracking: String = ""
        private var codeLanguage: String = "en"
        private var showAdsInter: Boolean = false
        private var items: List<LanguageItemModel> = listOf()

        fun setAdsLayoutRes(@LayoutRes res: Int) = apply { this.adsLayoutRes = res }
        fun setNameSpaceAds(name: String) = apply { this.nameSpaceAds = name }
        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setCodeLanguage(code: String) = apply { this.codeLanguage = code }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setItems(items: List<LanguageItemModel>) = apply { this.items = items }

        fun build() = LanguageConfig(
            adsLayoutRes, nameSpaceAds, nameTracking, codeLanguage, showAdsInter, items
        )
    }
}
