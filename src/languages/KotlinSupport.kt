package languages

import base.LanguageSupport

class KotlinSupport : LanguageSupport() {
    override fun getFileExtension(): String {
        return "kt"
    }

}