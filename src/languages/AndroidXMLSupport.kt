package languages

import base.LanguageSupport

class AndroidXMLSupport : LanguageSupport() {
    override fun getFileExtension(): String {
        return "xml"
    }
}