package base

import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import kotlinx.browser.document

abstract class LanguageSupport {

    var fileUrl: String? = null
    private val fullCode = document.querySelector("table.highlight tbody")?.textContent

    abstract fun getFileExtension(): String
    fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        for (feature in getFeatures()) {
            if (feature.isMatch(inputText, htmlSpanElement)) {
                feature.handle(inputText, htmlSpanElement, callback)
                return
            }
        }

        println("No match found")
        callback(null, false)

    }

    fun getFullCode(): String {
        requireNotNull(fullCode) { "fullCode is null. couldn't get code from website" }
        return fullCode
    }

    abstract fun getFeatures(): List<BaseFeature>
}