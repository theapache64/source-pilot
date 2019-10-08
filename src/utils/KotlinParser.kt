package utils

import org.w3c.dom.HTMLSpanElement


object KotlinParser {

    // Constants
    val IMPORT_PATTERN = "import (?<importStatement>[\\w\\.]+)".toRegex()
    private val PACKAGE_PATTERN = "package (?<importStatement>[\\w\\.]+)".toRegex()
    private val STRING_PATTERN = "\\.(.\\w+)\\)".toRegex()
    private val INTERNAL_METHOD_CALL_PATTERN = "^\\s*(?<methodName>\\w+)\\(".toRegex()
    private val VARIABLE_METHOD_CALL_PATTERN = "\\.\\s*\\w+\\s*".toRegex()

    fun parseImports(fullCode: String): List<String> {
        return IMPORT_PATTERN.findAll(fullCode).map { it.groups[1]?.value!! }.toList()
    }

    fun getCurrentPackageName(fullCode: String): String {
        return PACKAGE_PATTERN.find(fullCode)!!.groups[1]!!.value
    }

    fun getStringResName(inputText: String): String {
        return STRING_PATTERN.find(inputText)!!.groups[1]!!.value
    }

    /*fun isInternalMethodCall(inputText: String): Boolean {
        val matches = INTERNAL_METHOD_CALL_PATTERN.find(inputText)
        return matches != null
    }*/

    fun parseInternalMethodName(inputText: String): String {
        return INTERNAL_METHOD_CALL_PATTERN.find(inputText)!!.groups[1]!!.value
    }

    fun isExternalMethodCall(inputText: String, element: HTMLSpanElement): Boolean {
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

    fun parseImportPackage(importStatement: String): String? {
        return IMPORT_PATTERN.find(importStatement)!!.groups[1]!!.value
    }
}