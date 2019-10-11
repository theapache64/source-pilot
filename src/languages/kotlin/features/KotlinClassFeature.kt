package languages.kotlin.features

import base.LanguageSupport
import org.w3c.dom.HTMLSpanElement

/**
 * To navigate to external classes from class names
 */
open class KotlinClassFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return !isPrimitiveDataType(inputText) && imports.isNotEmpty()
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        gotoClass(inputText, htmlSpanElement, callback)
    }

    private fun isPrimitiveDataType(_inputText: String): Boolean {
        return when (_inputText.replace("?", "").toLowerCase()) {
            "boolean",
            "long",
            "float",
            "double",
            "char",
            "int",
            "string" -> true
            else -> false
        }
    }
}