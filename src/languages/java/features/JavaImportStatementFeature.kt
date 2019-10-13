package languages.java.features

import base.LanguageSupport
import languages.kotlin.features.KotlinImportStatementFeature
import org.w3c.dom.HTMLSpanElement

class JavaImportStatementFeature(languageSupport: LanguageSupport) : KotlinImportStatementFeature(languageSupport) {
    override fun isClickedOnEndClass(htmlSpanElement: HTMLSpanElement): Boolean {
        return getNextNonSpaceSiblingElement(htmlSpanElement)?.textContent?.trim() == ";"
    }
}