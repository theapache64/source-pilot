package utils

import base.LanguageSupport
import languages.xml.AndroidXMLSupport
import languages.java.JavaSupport
import languages.kotlin.KotlinSupport
import kotlinx.browser.window

/**
 * This class is responsible to handle language support
 */
class SupportManager {


    /**
     * To get file support for current file
     */
    fun getSupportForCurrentFile(): LanguageSupport? {

        val supportedLanguages = arrayOf(
                KotlinSupport(),
                JavaSupport(),
                AndroidXMLSupport()
        )

        val currentFileExt = CommonParser.parseFileExt(window.location.toString())
        for (support in supportedLanguages) {
            if (support.getFileExtension() == currentFileExt) {
                return support
            }
        }
        return null
    }

}