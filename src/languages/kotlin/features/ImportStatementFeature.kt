package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import languages.java.JavaSupport
import languages.kotlin.KotlinSupport
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.KotlinParser
import kotlin.browser.window

/**
 * To navigate to classes/directories from import statement
 */
class ImportStatementFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        val fullLine = htmlSpanElement.parentElement?.textContent ?: ""
        return KotlinParser.IMPORT_PATTERN.matches(fullLine)
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        println("Clicked on an import statement")

        val currentPackageName = KotlinParser.getCurrentPackageName(languageSupport.getFullCode())
        val importStatement = htmlSpanElement.parentElement?.textContent!!

        val isDir: Boolean
        val importPackage = if (isClickedOnEndClass(htmlSpanElement)) {
            isDir = false
            KotlinParser.parseImportPackage(importStatement)
        } else {
            // directory navigation
            isDir = true
            getDirectoryPackage(htmlSpanElement).trim()
        }

        if (isClickableImport(importPackage)) {
            gotoImport(currentPackageName, importPackage, isDir, callback)
        } else {
            callback(null, false)
        }
    }


    private fun isClickedOnEndClass(htmlSpanElement: HTMLSpanElement): Boolean {
        return if (languageSupport is JavaSupport) {
            val isNextSemiColon = getNextNonSpaceSiblingElement(htmlSpanElement)?.textContent?.trim() == ";"
            println("isNextSemiColon")
            isNextSemiColon
        } else {
            // java support
            getNextNonSpaceSiblingElement(htmlSpanElement) == null
        }
    }


    private fun getDirectoryPackage(htmlSpanElement: HTMLSpanElement): String {
        var s = ""
        var x: Element? = htmlSpanElement
        while (x != null) {
            val text = x.textContent
            if (text != null && text.trim() != "import") {
                s = "$text$s"
            }
            x = x.previousElementSibling
        }
        return s
    }
}