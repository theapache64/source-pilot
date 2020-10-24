package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import extensions.startsWithUppercaseLetter
import languages.kotlin.KotlinSupport
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get
import utils.CommonParser
import utils.KotlinParser
import kotlinx.browser.document
import kotlinx.browser.window

abstract class BaseKotlinFeature(languageSupport: LanguageSupport) : BaseFeature(languageSupport) {

    companion object {
        private const val DATA_BINDING_IMPORT_REGEX = ".*\\.databinding\\..+Binding"
    }

    protected val imports by lazy { KotlinParser.parseImports(languageSupport.getFullCode()) }


    /**
     * To check if the passed import passes all non matching conditions
     */
    protected fun isClickableImport(matchingImport: String?): Boolean {
        return matchingImport != null &&
                !matchingImport.startsWith("android.") &&
                !matchingImport.startsWith("java.") &&
                !matchingImport.startsWith("androidx.") &&
                !matchingImport.startsWith("kotlinx.android.synthetic.") &&
                !matchingImport.startsWith("com.google.android.material.") &&
                !isDataBindingImport(matchingImport)
    }


    private fun isDataBindingImport(matchingImport: String): Boolean {
        return matchingImport.matches(DATA_BINDING_IMPORT_REGEX)
    }

    protected fun gotoImport(currentPackageName: String, matchingImport: String?, isDir: Boolean, callback: (url: String?, isNewTab: Boolean) -> Unit, lineNumber: Int = 1) {

        val currentUrl = window.location.toString()
        val curFileExt = CommonParser.parseFileExt(currentUrl)
        val packageSlash = '/' + currentPackageName.replace('.', '/');
        val windowLocSplit = currentUrl.split(packageSlash)
        val fileExt = if (isDir) {
            ""
        } else {
            ".$curFileExt#L$lineNumber"
        }

        // Returning new url
        languageSupport.fileUrl = "${windowLocSplit[0]}/${matchingImport!!.replace('.', '/')}$fileExt"
        callback(languageSupport.fileUrl, true)
    }

    protected fun goto(lineNumber: Int, callback: (url: String?, isNewTab: Boolean) -> Unit) {
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


    protected fun gotoClass(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        val currentPackageName = KotlinParser.getCurrentPackageName(languageSupport.getFullCode())

        // Getting possible import statements for the class
        val matchingImport = getMatchingImport(inputText, currentPackageName, htmlSpanElement)
        println("Matching import is '$matchingImport'")

        when {

            isInnerInterfaceOrClass(inputText) -> {
                val lineNumber = getLineNumber(getContentRegEx(inputText))
                goto(lineNumber, callback)
            }

            isClickableImport(matchingImport) -> {
                gotoImport(currentPackageName, matchingImport, false, callback)
            }

            else -> {
                println("No import matched! Matching importing was : $matchingImport")
                callback(null, true)
            }
        }
    }

    /**
     * To get matching import for the input passed from the imports in the file.
     */
    private fun getMatchingImport(_inputText: String, currentPackageName: String, htmlSpanElement: HTMLSpanElement): String? {

        // Removing '?' from inputText. For eg: Bundle? -> Bundle
        val inputText = _inputText.replace("?", "")

        val matchingImports = imports.filter { it.endsWith(".$inputText") }
        println("Matching imports are : $matchingImports")
        return if (matchingImports.isNotEmpty()) {
            matchingImports.first()
        } else {
            println("No import matched for $inputText, setting current name : $currentPackageName")
            if (inputText.startsWithUppercaseLetter()) {
                "$currentPackageName.$inputText"
            } else {
                // Checking if it's the import statement it self
                println("Checking if it's import statement")
                val isImportStatement = htmlSpanElement.parentElement?.textContent.equals(getImportStatement(inputText))
                if (isImportStatement) {
                    inputText
                } else {
                    null
                }
            }
        }
    }

    private fun getImportStatement(importStatement: String): String {
        return "import $importStatement"
    }

    private fun isInnerInterfaceOrClass(inputText: String): Boolean {
        val fullCode = languageSupport.getFullCode()
        return fullCode.matches(getContentRegEx(inputText))
    }

    private fun getContentRegEx(inputText: String): String {
        return "(?:interface|class)\\s*$inputText\\s*[{(]"
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

    protected fun isRes(htmlSpanElement: HTMLSpanElement, res: String): Boolean {
        val clickedText = htmlSpanElement.textContent
        val fullLine = htmlSpanElement.parentElement?.textContent
        val semiSplit = fullLine?.split(";")?.get(0)
        if (semiSplit != null) {
            val isMatch = semiSplit.matches("R\\.$res\\.${clickedText?.replace("\\W+".toRegex(), "")}")
            println("is $clickedText is $res -> $isMatch")
            return isMatch
        }
        return true
    }


}