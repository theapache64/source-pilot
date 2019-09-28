package utils

object CommonParser {

    private val PATH_PATTERN = "github\\.com\\/[^\\/]+\\/[^\\/]+\\/(?<path>[^#\\n]+)".toRegex()
    private val EXT_PATTERN = "\\.(\\w+)".toRegex()


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