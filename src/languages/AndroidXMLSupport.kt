package languages

import base.LanguageSupport

class AndroidXMLSupport : LanguageSupport() {
    override fun getNewResourceUrl(inputText: String): String? {
        return null
    }

    override fun getFileExtension(): String {
        return "xml"
    }
}
