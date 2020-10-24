package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.KotlinParser
import utils.XMLLineFinder
import kotlinx.browser.window

/**
 * To navigate to strings.xml from `R.string.some_string` call
 */
open class KotlinStringResFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isRes(htmlSpanElement, "string")
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        val stringResName = getStringName(inputText)
        /*val currentUrl = window.location.toString()
        val stringXml = "${currentUrl.split("main")[0]}main/res/values/strings.xml"*/
        console.log("String name is '$stringResName'")
        val currentUrl = window.location.toString()
        val lastMainIndex = currentUrl.indexOf("/main/")
        val newUrl = currentUrl.substring(0, lastMainIndex) + "/main/res/values/strings.xml"
        callback(newUrl, true)
        XMLLineFinder.getLineNumber(newUrl, stringResName) { lineNumber ->
            if (lineNumber > 0) {
                callback("$newUrl#L$lineNumber", true)
            }else{
                callback(null, false)
            }
        }
    }

    open fun getStringName(inputText: String): String {
        return KotlinParser.getStringResName(inputText)
    }
}