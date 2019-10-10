package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import org.w3c.dom.HTMLSpanElement
import utils.KotlinParser

/**
 * To navigate to external classes from class names
 */
class ClassFeature(languageSupport: LanguageSupport) : BaseKotlinFeature(languageSupport) {

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        return imports.isNotEmpty()
    }

    override fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        gotoClass(inputText, htmlSpanElement, callback)
    }
}