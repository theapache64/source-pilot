package languages.java.features

import base.LanguageSupport
import languages.kotlin.features.KotlinInternalMethodCallFeature

class JavaInternalMethodCallFeature(languageSupport: LanguageSupport) : KotlinInternalMethodCallFeature(languageSupport) {
    override fun getMethodRegEx(methodName: String): String {
        return "(?:\\w+)?\\s+(?:\\w+)\\s+$methodName\\("
    }
}