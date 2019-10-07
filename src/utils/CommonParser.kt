package utils

import languages.KotlinSupport

object CommonParser {

    private val EXT_PATTERN = "\\.(\\w+)".toRegex()
    private val LAYOUT_NAME = "layout\\.(.+)\\)".toRegex()
    private val MENU_NAME = "menu\\.(.+)\\)".toRegex()
    private val XML_VALUE = "\\/([\\w.]+)".toRegex()
    private val LINE_NUMBER_REGEX = "(?<urlOnly>.+)#[L\\d\\-:]+\$".toRegex()

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

    fun parseMenuFileName(inputText: String): String? {
        return MENU_NAME.find(inputText)?.groups!![1]!!.value
    }

    fun parseValueName(inputText: String): String? {
        return XML_VALUE.find(inputText)?.groups!![1]!!.value
    }

    fun hasLineNumber(currentUrl: String): Boolean {
        return currentUrl.matches(LINE_NUMBER_REGEX)
    }

    fun parseUrlOnly(url: String): String {
        return LINE_NUMBER_REGEX.find(url)!!.groups[1]!!.value
    }
}