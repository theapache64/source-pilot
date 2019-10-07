package languages

import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get


class JavaSupport : KotlinSupport() {



    override fun getFileExtension(): String {
        return "java"
    }

    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        super.getNewResourceUrl(inputText, htmlSpanElement) { newUrl, isNewTab ->
            if (newUrl == null) {
                val matchedText = getUpperSiblingsSplitted(htmlSpanElement)
                if (matchedText != null) {
                    super.getNewResourceUrl(matchedText, htmlSpanElement, callback)
                } else {
                    callback(null, false)
                }
            } else {
                callback(newUrl, isNewTab)
            }
        }
    }



    override fun getImportStatement(importStatement: String): String {
        return "import $importStatement;"
    }
}
