package base

import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import kotlin.browser.document

abstract class LanguageSupport {

    private val fullCode = document.querySelector("table.highlight tbody")?.textContent

    abstract fun getFileExtension(): String
    abstract fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit)

    fun getFullCode(): String {
        requireNotNull(fullCode) { "fullCode is null. couldn't get code from website" }
        return fullCode
    }

    open fun getFeatures(): List<BaseFeature> {
        return listOf<BaseFeature>()
    }
}