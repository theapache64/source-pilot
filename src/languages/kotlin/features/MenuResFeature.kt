package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import kotlin.browser.window

class MenuResFeature(languageSupport: LanguageSupport) : BaseFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.previousElementSibling?.textContent.equals(".menu")
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        val menuFileName = getMenuFileName(inputText)
        val currentUrl = window.location.toString()
        val newUrl = getMenuUrl(currentUrl, menuFileName)
        callback(newUrl, true)
    }

    private fun getMenuFileName(inputText: String): String? {
        return CommonParser.parseMenuFileName(inputText)
    }

    private fun getMenuUrl(currentUrl: String, menuFileName: String?): String {
        val lastMainIndex = currentUrl.indexOf("/main/")
        return currentUrl.substring(0, lastMainIndex) + "/main/res/menu/$menuFileName.xml"
    }


}