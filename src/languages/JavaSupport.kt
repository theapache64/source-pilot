package languages

import base.LanguageSupport

class JavaSupport : LanguageSupport() {
    override fun getFileExtension(): String {
        return "java"
    }
}