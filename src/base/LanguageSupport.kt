package base

import kotlin.browser.document

abstract class LanguageSupport {

    private val fullCode = document.querySelector("table.highlight tbody")?.textContent

    abstract fun getFileExtension(): String
    abstract fun getNewResourceUrl(inputText: String): String?

    fun getFullCode(): String {
        requireNotNull(fullCode) { "fullCode is null. couldn't get code from website" }
        return fullCode
    }
}