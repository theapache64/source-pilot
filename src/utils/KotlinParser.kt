package utils


object KotlinParser {

    // Constants
    private val IMPORT_PATTERN = "import (?<importStatement>[\\w\\.]+)".toRegex()
    private val PACKAGE_PATTERN = "package (?<importStatement>[\\w\\.]+)".toRegex()
    private val STRING_PATTERN = "\\.string.(.\\w+)\\)".toRegex()
    private val INTERNAL_METHOD_CALL_PATTERN = "^\\s*(?<methodName>\\w+)\\(".toRegex()
    private val VARIABLE_METHOD_CALL_PATTERN = "(?<variableName>\\w+)\\s*\\.\\s*(?<methodName>\\w+)\\s*\\(".toRegex()

    fun parseImports(fullCode: String): List<String> {
        return IMPORT_PATTERN.findAll(fullCode).map { it.groups[1]?.value!! }.toList()
    }

    fun getCurrentPackageName(fullCode: String): String {
        return PACKAGE_PATTERN.find(fullCode)!!.groups[1]!!.value
    }

    fun getStringResName(inputText: String): String {
        return STRING_PATTERN.find(inputText)!!.groups[1]!!.value
    }

    fun isInternalMethodCall(inputText: String): Boolean {
        val matches = INTERNAL_METHOD_CALL_PATTERN.find(inputText)
        return matches != null
    }

    fun parseInternalMethodName(inputText: String): String {
        return INTERNAL_METHOD_CALL_PATTERN.find(inputText)!!.groups[1]!!.value
    }

    fun isExternalMethodCall(inputText: String): Boolean {
        return VARIABLE_METHOD_CALL_PATTERN.find(inputText) != null
    }
}