package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.LanguageItemModel

data class LanguageConfig(
    @LayoutRes val languageLayoutRes: Int,
    @LayoutRes val itemLanguageLayoutRes: Int,
    val adsLanguage: List<Pair<Int, String>>,
    val adsReload: List<Pair<Int, String>>,
    val autoAdsReload: Triple<Int, String, Int>,
    val nameTracking: String,
    val codeLanguage: String,
    val showAdsInter: Boolean,
    val showUiApply: Boolean,
    val items: List<LanguageItemModel> = listOf(),
) {
    class Builder {
        private var languageLayoutRes: Int = 0
        private var itemLanguageLayoutRes: Int = 0
        private var adsLanguage: List<Pair<Int, String>> = emptyList()
        private var adsReload: List<Pair<Int, String>> = emptyList()

        private var autoAdsReload: Triple<Int, String, Int> = Triple(0, "", 0)
        private var nameTracking: String = ""
        private var codeLanguage: String = ""
        private var showAdsInter: Boolean = false
        private var showUiApply: Boolean = false
        private var items: List<LanguageItemModel> = emptyList()

        fun setLanguageLayout(@LayoutRes res: Int) = apply { this.languageLayoutRes = res }
        fun setItemLanguageLayout(@LayoutRes res: Int) =
            apply { this.itemLanguageLayoutRes = res }

        fun setAdsLanguage(list: List<Pair<Int, String>>) = apply { this.adsLanguage = list }
        fun setAdsReload(list: List<Pair<Int, String>>) = apply { this.adsReload = list }
        fun setNameTracking(name: String) = apply { this.nameTracking = name }
        fun setCodeLanguage(code: String) = apply { this.codeLanguage = code }
        fun setShowAdsInter(show: Boolean) = apply { this.showAdsInter = show }
        fun setAutoAdsReload(autoAdsReload: Triple<Int, String, Int>) =
            apply { this.autoAdsReload = autoAdsReload }

        fun setShowUiApply(show: Boolean) = apply { this.showUiApply = show }
        fun setItems(items: List<LanguageItemModel>) = apply { this.items = items }

        fun build(): LanguageConfig {
            require(languageLayoutRes != 0) { "languageLayoutRes must be set" }
            require(itemLanguageLayoutRes != 0) { "itemLanguageLayoutRes must be set" }

            return LanguageConfig(
                languageLayoutRes = languageLayoutRes,
                itemLanguageLayoutRes = itemLanguageLayoutRes,
                adsLanguage = adsLanguage,
                adsReload = adsReload,
                nameTracking = nameTracking,
                codeLanguage = codeLanguage,
                showAdsInter = showAdsInter,
                showUiApply = showUiApply,
                items = items,
                autoAdsReload = autoAdsReload
            )
        }
    }
}
