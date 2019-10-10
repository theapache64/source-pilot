package utils

import org.w3c.dom.HTMLSpanElement


object KotlinParser {

    // Constants
    val IMPORT_PATTERN = "import (?<importStatement>[\\w\\.]+);?".toRegex()
    private val PACKAGE_PATTERN = "package (?<importStatement>[\\w\\.]+)".toRegex()
    private val STRING_PATTERN = "\\.(.\\w+)\\)".toRegex()
    private val INTERNAL_METHOD_CALL_PATTERN = "^\\s*(?<methodName>\\w+)\\(".toRegex()
    private const val ASSIGNED_FROM_PATTERN = "\\s*(?::|=)\\s*(?<assignedFrom>[\\w\\.\\(\\)]+)"

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



    fun parseImportPackage(importStatement: String): String? {
        return IMPORT_PATTERN.find(importStatement)!!.groups[1]!!.value
    }

    fun getAssignedFrom(fullCode: String, variableName: String?): String? {
        return getAssignedPattern(variableName).toRegex().findAll(fullCode).first().groups[1]?.value
    }

    fun getAssignedPattern(variableName: String?): String {
        return "$variableName$ASSIGNED_FROM_PATTERN"
    }
}