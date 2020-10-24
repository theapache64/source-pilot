package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get
import utils.KotlinParser
import kotlinx.browser.document

/**
 * To navigate to in-class variable type definition/ assignment line
 */
open class KotlinVariableFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isVariable(htmlSpanElement)
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        println("It's a variable")
        val assignLineNumber = getAssignedLineNumber(inputText)
        goto(assignLineNumber, callback)
    }

    private fun getAssignedLineNumber(variableName: String?): Int {
        val allTd = document.querySelectorAll("table.highlight tbody tr td.blob-code")
        val matchRegEx = KotlinParser.getAssignedPattern(variableName)
        println("RegEx is $matchRegEx")
        for (i in 0 until allTd.length) {
            val td = allTd[i] as Element
            val line = td.textContent
            if (line != null && line.matches(matchRegEx)) {
                return td.id.replace("LC", "").toInt()
            }
        }
        return -1
    }

    open fun isVariable(htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.className != "pl-en" &&
                htmlSpanElement.textContent?.matches("\\w+") ?: false &&
                getNextNonSpaceSiblingElement(htmlSpanElement)?.textContent?.startsWith(".") ?: false
    }



}