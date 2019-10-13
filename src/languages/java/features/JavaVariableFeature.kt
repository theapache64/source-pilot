package languages.java.features

import base.LanguageSupport
import languages.kotlin.features.KotlinVariableFeature
import org.w3c.dom.HTMLSpanElement

class JavaVariableFeature(languageSupport: LanguageSupport) : KotlinVariableFeature(languageSupport) {
    override fun isVariable(htmlSpanElement: HTMLSpanElement): Boolean {
        return super.isVariable(htmlSpanElement) &&
                getNextNonSpaceSiblingElement(htmlSpanElement)?.textContent?.equals("this") == false &&
                getNextNonSpaceSiblingElement(htmlSpanElement)?.className == "pl-c1"
    }
}