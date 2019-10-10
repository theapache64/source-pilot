package languages

import base.LanguageSupport
import core.BaseFeature
import extensions.startsWithUppercaseLetter
import languages.kotlin.features.*
import org.w3c.dom.*
import utils.CommonParser
import utils.KotlinLineFinder
import utils.KotlinParser
import utils.XMLLineFinder
import kotlin.browser.document
import kotlin.browser.window

/**
 * To support .kt (kotlin) files
 * This class will be extended by JavaSupport. All 'open' methods will be overriden in `JavaSupport`
 */
open class KotlinSupport : LanguageSupport() {

    companion object {
        protected const val LAYOUT_PREFIX = ".layout."
        protected const val STRING_PREFIX = ".string."
        protected const val MENU_PREFIX = ".menu."
    }

    var fileUrl: String? = null

    override fun getFeatures(): List<BaseFeature> {
        return listOf(
                LayoutResFeature(),
                StringResFeature(),
                MenuResFeature(),
                ImportStatementFeature(),
                InternalMethodCallFeature(),
                VariableFeature(),
                ExternalMethodCallFeature(),
                ClassFeature()
        )
    }


    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        if (!isKotlinDataType(inputText)) {

            for (feature in getFeatures()) {
                if (feature.isMatch(inputText, htmlSpanElement)) {
                    feature.handle(this, inputText, htmlSpanElement, callback)
                    return
                }
            }

            if (imports.isNotEmpty()) {
                gotoClass(inputText, htmlSpanElement, callback)
            } else {
                println("No match found")
                callback(null, true)
            }


        } else {
            // it was a kotlin data type
            callback(null, true)
        }
    }


    open fun getMenuFileName(inputText: String): String? {
        return CommonParser.parseMenuFileName(inputText)
    }

    protected fun getNextNonSpaceSiblingElement(htmlSpanElement: HTMLElement): Element? {
        var x = htmlSpanElement.nextElementSibling
        while (x != null) {
            if (x.textContent?.isNotBlank() == true) {
                return x
            }
            x = x.nextElementSibling
        }
        return null
    }

    protected open fun getImportStatement(importStatement: String): String {
        return "import $importStatement"
    }

    open fun isImportStatement(htmlSpanElement: HTMLSpanElement): Boolean {
        val fullLine = htmlSpanElement.parentElement?.textContent ?: ""
        println("IMPORT: full import line is $fullLine")
        return KotlinParser.IMPORT_PATTERN.matches(fullLine)
    }


    open fun isMenuRes(htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.previousElementSibling?.textContent.equals(".menu")
    }

    open fun isStringRes(htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.previousElementSibling?.textContent.equals(".string")
    }

    open fun isLayoutName(htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.previousElementSibling?.textContent.equals(".layout")
    }

    private fun getSupportableElementFromReverse(_htmlSpanElement: HTMLSpanElement): List<String> {
        val arr = mutableListOf<String>()
        var endNode = _htmlSpanElement as Element?
        while (endNode != null) {
            arr.add(endNode.textContent!!)
            endNode = endNode.previousElementSibling
        }

        val x = mutableListOf<String>()
        for (arrElement in arr.withIndex()) {
            val subList = arr.subList(0, arrElement.index).reversed()
            val newElement = subList.joinToString(separator = "")
            if (newElement.isNotBlank()) {
                x.add(newElement)
            }
        }
        return x
    }


    private fun getLineNumber(regex: String): Int {
        val tdBlobCodes = document.querySelectorAll("table.highlight tbody tr td.blob-code")
        for (tdIndex in 0 until tdBlobCodes.length) {
            val td = tdBlobCodes[tdIndex] as Element
            val codeLine = td.textContent
            if (codeLine != null && codeLine.trim().isNotEmpty()) {
                val isMatch = codeLine.matches(regex)
                if (isMatch) {
                    return td.id.replace("LC", "").toInt()
                }
            }
        }
        return -1
    }


    private fun isKotlinDataType(_inputText: String): Boolean {
        return when (_inputText.replace("?", "")) {
            "Boolean",
            "Long",
            "Float",
            "Double",
            "Char",
            "Int",
            "String" -> true
            else -> false
        }
    }


    override fun getFileExtension(): String {
        return "kt"
    }




}