package utils

import base.LanguageSupport
import languages.AndroidXMLSupport
import languages.JavaSupport
import languages.KotlinSupport
import kotlin.browser.window

/**
 * This class is responsible to handle language support
 */
class SupportManager {

    companion object {
        /**
         * Supported files/technology by source-pilot
         */
        private val SUPPORTS = arrayOf(
                KotlinSupport(),
                JavaSupport(),
                AndroidXMLSupport()
        )
    }

    /**
     * To get file support for current file
     */
    fun getSupportForCurrentFile(): LanguageSupport {
        val currentFileExt = CommonParser.parseFileExt(window.location.toString())
        for (support in SUPPORTS) {
            if (support.getFileExtension() == currentFileExt) {
                return support
            }
        }
        throw IllegalArgumentException("No support found for $currentFileExt")
    }

}