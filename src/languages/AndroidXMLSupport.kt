package languages

import base.LanguageSupport
import org.w3c.dom.HTMLSpanElement
import org.w3c.xhr.XMLHttpRequest
import utils.CommonParser
import kotlin.browser.window

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
            else -> null
        }

        if (fileName != null) {
            val fileUrl = "${currentUrl.split("main")[0]}main/res/$fileName"
            callback(fileUrl)
            // Getting line number
            val valueName = CommonParser.parseValueName(inputText)
            if (valueName != null && !fileName.startsWith("drawable")) {
                println("Getting line number..,.")
                getLineNumber(fileUrl, valueName) { lineNumber ->
                    callback("$fileUrl#L$lineNumber")
                }
            }
        } else {
            callback(null)
        }
    }

    private fun getLineNumber(fileUrl: String, valueName: String, callback: (Int) -> Unit) {
        // normal : https://github.com/theapache64/swipenetic/blob/master/app/src/main/res/values/strings.xml
        // raw : https://raw.githubusercontent.com/theapache64/swipenetic/master/app/src/main/res/values/strings.xml
        val rawUrl = fileUrl.replace("https://github.com", "https://raw.githubusercontent.com").replaceFirst("/blob", "")
        val xhr = XMLHttpRequest()
        xhr.open("GET", rawUrl)
        xhr.onreadystatechange = {
            if (xhr.readyState.toInt() == 4 && xhr.status.toInt() == 200) {
                val lineNumber = getLineNumber(xhr.responseText, valueName)
                callback(lineNumber)
            }
        };
        xhr.send()
    }

    private fun getLineNumber(data: String, value: String): Int {
        console.log("Searching for ", value)
        val x = data.split("\n")
        x.forEachIndexed { index, line ->
            println("Line -> $line")
            if (line.contains("\"$value\"")) {
                println("Found")
                return index + 1
            }
        }
        return 1
    }

    override fun getFileExtension(): String {
        return "xml"
    }
}
