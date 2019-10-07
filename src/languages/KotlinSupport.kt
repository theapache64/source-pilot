package languages

import base.LanguageSupport
import extensions.startsWithUppercaseLetter
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get
import utils.CommonParser
import utils.KotlinParser
import utils.XMLLineFinder
import kotlin.browser.document
import kotlin.browser.window

open class KotlinSupport : LanguageSupport() {

    companion object {
        private const val DATA_BINDING_IMPORT_REGEX = ".*\\.databinding\\..+Binding"
        protected const val LAYOUT_PREFIX = ".layout."
        protected const val STRING_PREFIX = ".string."
        protected const val MENU_PREFIX = ".menu."
    }

    private val imports by lazy { KotlinParser.parseImports(getFullCode()) }

    /**
     * To get matching import for the input passed from the imports in the file.
     */
    private fun getMatchingImport(_inputText: String, currentPackageName: String, htmlSpanElement: HTMLSpanElement): String? {

        // Removing question mark (optional-kotlin)
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

    protected open fun getImportStatement(importStatement: String): String {
        return "import $importStatement"
    }


    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        if (!isKotlinDataType(inputText)) {

            if (inputText.startsWith(LAYOUT_PREFIX)) {
                println("Generating new url for layout : $inputText")
                val layoutFileName = CommonParser.parseLayoutFileName(inputText)
                val currentUrl = window.location.toString()
                callback("${currentUrl.split("main")[0]}main/res/layout/$layoutFileName.xml", true)
            } else if (inputText.startsWith(STRING_PREFIX)) {
                val stringResName = KotlinParser.getStringResName(inputText)
                val currentUrl = window.location.toString()
                val stringXml = "${currentUrl.split("main")[0]}main/res/values/strings.xml"
                callback(stringXml, true)
                XMLLineFinder.getLineNumber(stringXml, stringResName) { lineNumber ->
                    callback("$stringXml#L$lineNumber", true)
                }
            } else if (inputText.startsWith(MENU_PREFIX)) {
                println("Generating new url for menu : $inputText")
                val menuFileName = CommonParser.parseMenuFileName(inputText)
                val currentUrl = window.location.toString()
                callback("${currentUrl.split("main")[0]}main/res/menu/$menuFileName.xml", true)
            } else if (KotlinParser.isInternalMethodCall(inputText)) {

                println("Internal method call..")

                // internal method call
                val methodName = KotlinParser.parseInternalMethodName(inputText)
                val lineNumbers = getMethodDefinitionLineNumber(methodName)
                if (lineNumbers.isNotEmpty()) {
                    if (lineNumbers.size == 1) {
                        val lineNumber = lineNumbers.first()
                        if (lineNumber > 0) {
                            var currentUrl = window.location.toString()
                            if (CommonParser.hasLineNumber(currentUrl)) {
                                currentUrl = CommonParser.parseUrlOnly(currentUrl)
                            }

                            callback("$currentUrl#L$lineNumber", false)
                        } else {
                            callback(null, false)
                        }
                    } else {
                        // multiple method definitions
                        callback(null, false)
                        htmlSpanElement.setAttribute("sp-error", "Selected method has multiple definitions")
                    }

                } else {
                    callback(null, false)
                }
            } else if (imports.isNotEmpty()) {

                val currentPackageName = KotlinParser.getCurrentPackageName(getFullCode())

                // Getting possible import statements for the class
                val matchingImport = getMatchingImport(inputText, currentPackageName, htmlSpanElement)

                if (isClickableImport(matchingImport)) {
                    val currentUrl = window.location.toString()
                    val curFileExt = CommonParser.parseFileExt(currentUrl)
                    val packageSlash = '/' + currentPackageName.replace('.', '/');
                    val windowLocSplit = currentUrl.split(packageSlash)

                    // Returning new url
                    callback("${windowLocSplit[0]}/${matchingImport!!.replace('.', '/')}.$curFileExt#L1", true)
                } else {
                    println("No import matched! Matching importing was : $matchingImport")
                    callback(null, true)
                }
            } else {
                println("No match found")
                callback(null, true)
            }
        } else {
            println("It was a kotlin data type")
            callback(null, true)
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

    private fun getMethodRegEx(methodName: String): String {
        return "fun\\s*$methodName\\s*\\("
    }

    private fun isKotlinDataType(_inputText: String): Boolean {
        val inputText = _inputText.replace("?", "")
        return when (inputText) {
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

    /**
     * To check if the passed import passes all non matching conditions
     */
    private fun isClickableImport(matchingImport: String?): Boolean {
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

    override fun getFileExtension(): String {
        return "kt"
    }

}