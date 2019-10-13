package languages.java.features

import base.LanguageSupport
import languages.kotlin.features.KotlinStringResFeature
import utils.JavaParser

class JavaStringResFeature(languageSupport: LanguageSupport) : KotlinStringResFeature(languageSupport) {
    override fun getStringName(inputText: String): String {
        return JavaParser.parseStringName(inputText)
    }
}