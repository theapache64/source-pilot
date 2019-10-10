package core

import base.LanguageSupport
import org.w3c.dom.HTMLSpanElement

abstract class BaseFeature(val languageSupport: LanguageSupport) {
    abstract fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean
    abstract fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit)
}