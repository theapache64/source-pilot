package utils

import base.LanguageSupport
import languages.xml.AndroidXMLSupport
import languages.JavaSupport
import languages.kotlin.KotlinSupport
import kotlin.browser.window

/**
 * This class is responsible to handle language support
 */
class SupportManager {


    /**
     * To get file support for current file
     */
    fun getSupportForCurrentFile(): LanguageSupport? {

        val arrayOfLanguageSupports = arrayOf(
                KotlinSupport(),
                JavaSupport(),
                AndroidXMLSupport()
        )

        val currentFileExt = CommonParser.parseFileExt(window.location.toString())
        for (support in arrayOfLanguageSupports) {
            if (support.getFileExtension() == currentFileExt) {
                return support
            }
        }
        return null
    }

}