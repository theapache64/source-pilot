package languages

import base.LanguageSupport
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.XMLLineFinder
import kotlin.browser.window

/**
 * This class is used to support Android XML
 */
class AndroidXMLSupport : LanguageSupport() {

    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (String?) -> Unit) {
        val currentUrl = window.location.toString()

        val fileName = when {
            inputText.matches("@style\\/.+") -> "values/styles.xml"
            inputText.matches("@string\\/.+") -> "values/strings.xml"
            inputText.matches("@color\\/.+") -> "values/colors.xml"
            inputText.matches("@dimen/.+") -> "values/dimens.xml"
            inputText.matches("@drawable\\/.+") -> {
                val fileName = CommonParser.parseValueName(inputText)
                "drawable/$fileName.xml"
            }
            inputText.matches("@layout\\/.+") -> {
                val fileName = CommonParser.parseValueName(inputText)
                "layout/$fileName.xml"
            }
            else -> null
        }

        if (fileName != null) {
            val fileUrl = "${currentUrl.split("main")[0]}main/res/$fileName"
            callback(fileUrl)
            // Getting line number
            val valueName = CommonParser.parseValueName(inputText)
            if (valueName != null && !fileName.startsWith("drawable")) {
                println("Getting line number..,.")
                XMLLineFinder.getLineNumber(fileUrl, valueName) { lineNumber ->
                    callback("$fileUrl#L$lineNumber")
                }
            }
        } else {
            callback(null)
        }
    }


    override fun getFileExtension(): String {
        return "xml"
    }
}
