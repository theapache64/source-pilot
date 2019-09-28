package languages

import base.LanguageSupport
import org.w3c.dom.HTMLSpanElement

class AndroidXMLSupport : LanguageSupport() {

    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement): String? {
        return null
    }

    override fun getFileExtension(): String {
        return "xml"
    }
}
