package utils


object KotlinParser {

    // Constants
    private val IMPORT_PATTERN = "import (?<importStatement>[\\w\\.]+)".toRegex()
    private val PACKAGE_PATTERN = "package (?<importStatement>[\\w\\.]+)".toRegex()
    private val STRING_PATTERN = "\\.string.(.\\w+)\\)".toRegex()

    fun parseImports(fullCode: String): List<String> {
        return IMPORT_PATTERN.findAll(fullCode).map { it.groups[1]?.value!! }.toList()
    }

    fun getCurrentPackageName(fullCode: String): String {
        return PACKAGE_PATTERN.find(fullCode)!!.groups[1]!!.value
    }

    fun getStringResName(inputText: String): String {
        return STRING_PATTERN.find(inputText)!!.groups[1]!!.value
    }
}