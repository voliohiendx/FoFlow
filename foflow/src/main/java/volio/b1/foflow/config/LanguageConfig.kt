package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.LanguageItemModel

data class LanguageConfig(
    @LayoutRes val adsLayoutRes: List<Int>,
    val nameSpaceAds: List<String>,
    @LayoutRes val adsLayoutResReload: List<Int>,
    val nameSpaceAdsReload: List<String>,
    val nameTracking: String,
    val codeLanguage: String,
    val showAdsInter: Boolean,
    val showUiApply: Boolean,
    val items: List<LanguageItemModel> = listOf()
) {
    class Builder {
        private var adsLayoutRes: List<Int> = emptyList()
        private var nameSpaceAds: List<String> = emptyList()
        private var adsLayoutResReload: List<Int> = emptyList()
        private var nameSpaceAdsReload: List<String> = emptyList()
        private var nameTracking: String = ""
        private var codeLanguage: String = "en"
        private var showAdsInter: Boolean = false
        private var showUiApply: Boolean = false
        private var items: List<LanguageItemModel> = emptyList()

        fun setAdsLayoutRes(@LayoutRes res: List<Int>) = apply { this.adsLayoutRes = res }
        fun setNameSpaceAds(list: List<String>) = apply { this.nameSpaceAds = list }
        fun setAdsLayoutResReload(@LayoutRes res: List<Int>) = apply { this.adsLayoutResReload = res }
        fun setNameSpaceAdsReload(list: List<String>) = apply { this.nameSpaceAdsReload = list }

        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setCodeLanguage(code: String) = apply { this.codeLanguage = code }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setShowUiApply(show: Boolean) = apply { this.showUiApply = show }

        fun setItems(items: List<LanguageItemModel>) = apply { this.items = items }

        fun build() = LanguageConfig(
            adsLayoutRes = adsLayoutRes,
            nameSpaceAds = nameSpaceAds,
            adsLayoutResReload = adsLayoutResReload,
            nameSpaceAdsReload = nameSpaceAdsReload,
            nameTracking = nameTracking,
            codeLanguage = codeLanguage,
            showAdsInter = showAdsInter,
            showUiApply = showUiApply,
            items = items
        )
    }
}
