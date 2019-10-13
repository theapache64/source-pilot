package languages.java.features

import base.LanguageSupport
import languages.kotlin.features.KotlinMenuResFeature

class JavaMenuResFeature(languageSupport: LanguageSupport) : KotlinMenuResFeature(languageSupport) {
    override fun getMenuFileName(inputText: String): String? {
        println("Menu file name is $inputText")
        return inputText
    }
}