package languages

import base.LanguageSupport


class JavaSupport : KotlinSupport() {
    override fun getFileExtension(): String {
        return "java"
    }
}
