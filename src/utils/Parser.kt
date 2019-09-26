package utils


object Parser {

    // Constants
    private val IMPORT_PATTERN = "import (?<importStatement>[\\w\\.]+)".toRegex()
    private val PACKAGE_PATTERN = "package (?<importStatement>[\\w\\.]+)".toRegex()
    private val PATH_PATTERN = "github\\.com\\/[^\\/]+\\/[^\\/]+\\/(?<path>[^#\\n]+)".toRegex()
    private val EXT_PATTERN = "\\.(\\w+)".toRegex()

    fun parseImports(fullCode: String): List<String> {
        return IMPORT_PATTERN.findAll(fullCode).map { it.groups[1]?.value!! }.toList()
    }

    fun currentPackageName(fullCode: String): String {
        return PACKAGE_PATTERN.find(fullCode)!!.groups[1]!!.value
    }

    fun getCurrentFilePath(fullUrl: String): String {
        return PATH_PATTERN.find(fullUrl)!!.groups[1]!!.value
    }

    fun parseFileExt(currentUrl: String): String? {
        val lastResult = EXT_PATTERN.findAll(currentUrl).lastOrNull()
        if (lastResult != null) {
            return lastResult.groups[1]!!.value
        }
        return null
    }
}