package languages.xml.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.XMLLineFinder
import kotlin.browser.window

class MiscFeatures(languageSupport: LanguageSupport) : BaseFeature(languageSupport) {

    private var fileName: String? = null

    override fun isMatch(_inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {

        val inputText = htmlSpanElement.parentElement?.textContent ?: ""

        this.fileName = when {
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

        println("The match is $fileName")

        return this.fileName != null
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        val currentUrl = window.location.toString()
        val fileUrl = "${currentUrl.split("main")[0]}main/res/$fileName"
        callback(fileUrl, true)
        println("File url is $fileUrl")
        // Getting line number
        val valueName = CommonParser.parseValueName(inputText)
        println("Value name is $valueName")
        if (valueName != null && hasLineNumber(inputText)) {
            println("Getting line number..,.")
            XMLLineFinder.getLineNumber(fileUrl, valueName) { lineNumber ->
                if (lineNumber > 0) {
                    callback("$fileUrl#L$lineNumber", true)
                } else {
                    callback(null, false)
                }
            }
        }
    }

    private fun hasLineNumber(valueName: String): Boolean {
        return valueName.matches("@string/") ||
                valueName.matches("@style/") ||
                valueName.matches("@dimen/") ||
                valueName.matches("@color/")
    }
}