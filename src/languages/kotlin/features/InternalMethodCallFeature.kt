package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get
import utils.CommonParser
import kotlin.browser.document
import kotlin.browser.window

class InternalMethodCallFeature : BaseFeature {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isInternalMethodCall(htmlSpanElement)
    }

    override fun handle(languageSupport: LanguageSupport, inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        // internal method call
        val methodName = inputText.trim()
        val lineNumbers = getMethodDefinitionLineNumber(methodName)
        if (lineNumbers.isNotEmpty()) {
            if (lineNumbers.size == 1) {
                val lineNumber = lineNumbers.first()
                goto(lineNumber, callback)
            } else {
                // multiple method definitions
                callback(null, false)
                htmlSpanElement.setAttribute("sp-error", "Selected method has multiple definitions")
            }

        } else {
            callback(null, false)
        }

    }

    private fun getMethodDefinitionLineNumber(methodName: String): List<Int> {
        val lineNumbers = mutableListOf<Int>()
        val tdBlobCodes = document.querySelectorAll("table.highlight tbody tr td.blob-code")
        for (tdIndex in 0 until tdBlobCodes.length) {
            val td = tdBlobCodes[tdIndex] as Element
            val codeLine = td.textContent
            if (codeLine != null && codeLine.trim().isNotEmpty()) {
                val regex = getMethodRegEx(methodName)
                val isMatch = codeLine.matches(regex)
                if (isMatch) {
                    val lineNumber = td.id.replace("LC", "").toInt()
                    lineNumbers.add(lineNumber)
                }
            }
        }
        return lineNumbers
    }

    private fun isInternalMethodCall(htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.nextElementSibling?.textContent?.startsWith("(") ?: false
                && htmlSpanElement.className != "pl-en"
                && htmlSpanElement.previousElementSibling?.textContent?.isBlank() ?: true
    }

    private fun getMethodRegEx(methodName: String): String {
        return "fun\\s*$methodName\\s*\\("
    }


    private fun goto(lineNumber: Int, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        if (lineNumber > 0) {
            var currentUrl = window.location.toString()
            if (CommonParser.hasLineNumber(currentUrl)) {
                currentUrl = CommonParser.parseUrlOnly(currentUrl)
            }

            callback("$currentUrl#L$lineNumber", false)
        } else {
            callback(null, false)
        }
    }
}