package utils

object CommonParser {

    private val PATH_PATTERN = "github\\.com\\/[^\\/]+\\/[^\\/]+\\/(?<path>[^#\\n]+)".toRegex()
    private val EXT_PATTERN = "\\.(\\w+)".toRegex()
    private val LAYOUT_NAME = "layout\\.(.+)\\)".toRegex()
    private val XML_VALUE = "\\/(\\w+)".toRegex()


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

    fun parseLayoutFileName(inputText: String): String? {
        return LAYOUT_NAME.find(inputText)?.groups!![1]!!.value
    }

    fun parseValueName(inputText: String): String? {
        return XML_VALUE.find(inputText)?.groups!![1]!!.value
    }
}