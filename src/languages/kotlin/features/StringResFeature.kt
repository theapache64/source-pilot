package languages.kotlin.features

import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.KotlinParser
import utils.XMLLineFinder
import kotlin.browser.window

class StringResFeature : BaseFeature {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.previousElementSibling?.textContent.equals(".string")
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        val stringResName = getStringName(inputText)
        val currentUrl = window.location.toString()
        val stringXml = "${currentUrl.split("main")[0]}main/res/values/strings.xml"
        callback(stringXml, true)
        XMLLineFinder.getLineNumber(stringXml, stringResName) { lineNumber ->
            callback("$stringXml#L$lineNumber", true)
        }
    }

    private fun getStringName(inputText: String): String {
        return KotlinParser.getStringResName(inputText)
    }
}