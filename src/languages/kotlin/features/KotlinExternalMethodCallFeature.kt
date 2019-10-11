package languages.kotlin.features

import base.LanguageSupport
import languages.kotlin.KotlinSupport
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import utils.KotlinLineFinder
import utils.KotlinParser

/**
 * To navigate to external method call definition (method calls to another classes using objects)
 */
open class KotlinExternalMethodCallFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    companion object {
        private val VARIABLE_METHOD_CALL_PATTERN = "\\.\\s*\\w+\\s*".toRegex()
    }

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return isExternalMethodCall(inputText, htmlSpanElement)
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        println("$inputText is an external method call")
        val variableName = getVariableName(htmlSpanElement)
        println("Variable name is $variableName")
        if (variableName != null) {
            val variableType = getVariableType(variableName)
            if (isClassName(variableType)) {
                println("Class name is $variableType")
                gotoClass(variableType!!, htmlSpanElement, callback)

                // Getting line number
                val fileUrl = languageSupport.fileUrl
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


    private fun getVariableType(variableName: String?): String? {
        return KotlinParser.getAssignedFrom(languageSupport.getFullCode(), variableName)
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


}