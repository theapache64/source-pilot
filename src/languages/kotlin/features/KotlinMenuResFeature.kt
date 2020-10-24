package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import kotlinx.browser.window

/**
 * To navigate to menu resource from `R.menu.menu_file` call
 */
open class KotlinMenuResFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isRes(htmlSpanElement, "menu")
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        val menuFileName = getMenuFileName(inputText)
        val currentUrl = window.location.toString()
        val newUrl = getMenuUrl(currentUrl, menuFileName)
        callback(newUrl, true)
    }

    open fun getMenuFileName(inputText: String): String? {
        return CommonParser.parseMenuFileName(inputText)
    }

    private fun getMenuUrl(currentUrl: String, menuFileName: String?): String {
        val lastMainIndex = currentUrl.indexOf("/main/")
        return currentUrl.substring(0, lastMainIndex) + "/main/res/menu/$menuFileName.xml"
    }


}