package core

import org.w3c.dom.HTMLSpanElement

interface BaseFeature {
    fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean
    fun handle(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit)
}