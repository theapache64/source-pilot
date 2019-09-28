package base

import org.w3c.dom.HTMLSpanElement
import kotlin.browser.document

abstract class LanguageSupport {

    private val fullCode = document.querySelector("table.highlight tbody")?.textContent

    abstract fun getFileExtension(): String
    abstract fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (String?) -> Unit)

    fun getFullCode(): String {
        requireNotNull(fullCode) { "fullCode is null. couldn't get code from website" }
        return fullCode
    }


}