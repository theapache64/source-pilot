package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import kotlinx.browser.window

/**
 * To navigate to layout from `R.layout.layout_file` call
 */
open class KotlinLayoutResFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isLayoutName(htmlSpanElement)
    }

    open fun isLayoutName(htmlSpanElement: HTMLSpanElement): Boolean {
        return isRes(htmlSpanElement, "layout")
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        val layoutFileName = CommonParser.parseLayoutFileName(inputText)
        val currentUrl = window.location.toString()

        val lastMainIndex = currentUrl.indexOf("/main/")
        val newUrl = currentUrl.substring(0, lastMainIndex) + "/main/res/layout/$layoutFileName.xml"
        callback(newUrl, true)
    }
}


