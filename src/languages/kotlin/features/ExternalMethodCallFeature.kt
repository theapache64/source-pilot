package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import extensions.startsWithUppercaseLetter
import languages.KotlinSupport
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import utils.KotlinLineFinder
import utils.KotlinParser

class ExternalMethodCallFeature : BaseFeature {

    companion object {
        private val VARIABLE_METHOD_CALL_PATTERN = "\\.\\s*\\w+\\s*".toRegex()
    }

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isExternalMethodCall(inputText, htmlSpanElement)
    }

    override fun handle(languageSupport: LanguageSupport, inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        println("$inputText is an external method call")
        val variableName = getVariableName(htmlSpanElement)
        println("Variable name is $variableName")
        if (variableName != null) {
            val variableType = getVariableType(variableName)
            if (isClassName(variableType)) {
                println("Class name is $variableType")
                gotoClass(variableType!!, htmlSpanElement, callback)

                // Getting line number
                val fileUrl = (languageSupport as KotlinSupport).fileUrl
                if (fileUrl != null) {
                    KotlinLineFinder.getLineNumber(fileUrl, getFunRegEx(inputText.replace(".", ""))) { lineNumber ->
                        val newUrl = "${fileUrl.replace("#L.+".toRegex(), "")}#L$lineNumber"
                        callback(newUrl, true)
                    }
                }
            } else {
                println("Non class variable types, such as method calls will supported in future")
                callback(null, false)
            }
        } else {
            callback(null, false)
        }

    }

    private fun getFunRegEx(methodName: String): String {
        return "fun\\s+$methodName\\s*\\("
    }


    private fun getVariableType(languageSupport: LanguageSupport, variableName: String?): String? {
        return KotlinParser.getAssignedFrom(languageSupport.getFullCode(), variableName)
    }

    private fun isAssignedViaMethod(assignedFrom: String): Boolean {
        return assignedFrom.matches("(?<variableName>\\w+)\\s*.\\s*(?<methodName>\\w+)")
    }

    private fun isClassName(assignedFrom: String?): Boolean {
        return assignedFrom?.matches("\\w+") ?: false
    }


    private fun getPreviousNonSpaceSiblingElement(htmlSpanElement: Element): Element? {
        var x = htmlSpanElement.previousElementSibling
        while (x != null) {
            if (x.textContent?.isNotBlank() == true) {
                return x
            }
            x = x.previousElementSibling
        }
        return null
    }

    private fun getVariableName(htmlSpanElement: HTMLSpanElement): String? {
        return getPreviousNonSpaceSiblingElement(htmlSpanElement)?.textContent
    }

    private fun isExternalMethodCall(inputText: String, element: HTMLSpanElement): Boolean {
        val isMethodCall = VARIABLE_METHOD_CALL_PATTERN.matches(inputText)
        var hasMethodVariable = false
        var x = element.previousElementSibling
        while (x != null) {
            val xContent = x.textContent?.trim()
            if (xContent?.matches("\\w+") == true) {
                hasMethodVariable = true
                break
            } else {
                x = x.previousElementSibling
            }
        }
        return isMethodCall && hasMethodVariable
    }

    private fun gotoClass(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        val currentPackageName = KotlinParser.getCurrentPackageName(getFullCode())

        // Getting possible import statements for the class
        val matchingImport = getMatchingImport(inputText, currentPackageName, htmlSpanElement)

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

    private fun isInnerInterfaceOrClass(kotlinSupport: KotlinSupport, inputText: String): Boolean {
        val fullCode = kotlinSupport.getFullCode()
        return fullCode.matches(getContentRegEx(inputText))
    }

    private fun getContentRegEx(inputText: String): String {
        return "(?:interface|class)\\s*$inputText\\s*[{(]"
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
}