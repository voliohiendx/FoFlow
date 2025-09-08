package volio.b1.foflow.config

import androidx.annotation.LayoutRes
import volio.b1.foflow.model.LanguageItemModel
import volio.b1.foflow.R

object LanguageConfig {
    var activityLayoutRes: Int = R.layout.activity_language_default
    var itemLayoutRes: Int = R.layout.item_language_default

    var adsLayoutRes: Int = R.layout.native_ads_default
    var adsLayoutResDefault: Int = R.layout.native_ads_default

    var nameSpaceAds: String = ""

    var nameTracking: String = ""

    var codeLanguage = "en"
    var items: List<LanguageItemModel> = listOf(
        LanguageItemModel("vi", R.drawable.flag_england_demo, "vi_VN"),
        LanguageItemModel("en", R.drawable.flag_england_demo, "vi_VN"),
        LanguageItemModel("de", R.drawable.flag_england_demo, "vi_VN"),
    )

    fun initData(
        @LayoutRes activityLayoutRes: Int,
        @LayoutRes itemLayoutRes: Int,
        @LayoutRes adsLayoutRes: Int,
        @LayoutRes adsLayoutResDefault: Int,
        nameSpaceAds: String,
        nameTracking: String,
        codeLanguage: String,
        items: List<LanguageItemModel>
    ) {
        this.activityLayoutRes = activityLayoutRes
        this.itemLayoutRes = itemLayoutRes
        this.adsLayoutRes = adsLayoutRes
        this.nameSpaceAds = nameSpaceAds
        this.nameTracking = nameTracking
        this.codeLanguage = codeLanguage
        this.items = items
        this.adsLayoutResDefault = adsLayoutResDefault
    }
}