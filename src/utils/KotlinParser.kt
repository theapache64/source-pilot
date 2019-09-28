package utils



object KotlinParser {

    // Constants
    private val IMPORT_PATTERN = "import (?<importStatement>[\\w\\.]+)".toRegex()
    private val PACKAGE_PATTERN = "package (?<importStatement>[\\w\\.]+)".toRegex()


    fun parseImports(fullCode: String): List<String> {
        return IMPORT_PATTERN.findAll(fullCode).map { it.groups[1]?.value!! }.toList()
    }

    fun currentPackageName(fullCode: String): String {
        return PACKAGE_PATTERN.find(fullCode)!!.groups[1]!!.value
    }
}