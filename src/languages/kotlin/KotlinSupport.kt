package languages.kotlin

import base.LanguageSupport
import core.BaseFeature
import languages.kotlin.features.*
import org.w3c.dom.*
import utils.CommonParser
import utils.KotlinParser
import kotlin.browser.document

/**
 * To support .kt (kotlin) files
 * This class will be extended by JavaSupport. All 'open' methods will be overriden in `JavaSupport`
 */
open class KotlinSupport : LanguageSupport() {

    var fileUrl: String? = null

    override fun getFeatures(): List<BaseFeature> {
        return listOf(
                LayoutResFeature(this),
                StringResFeature(this),
                MenuResFeature(this),
                ImportStatementFeature(this),
                InternalMethodCallFeature(this),
                VariableFeature(this),
                ExternalMethodCallFeature(this),
                ClassFeature(this)
        )
    }


    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {

        if (!isKotlinDataType(inputText)) {

            for (feature in getFeatures()) {
                if (feature.isMatch(inputText, htmlSpanElement)) {
                    println("Matched with ${feature::class.simpleName}")
                    feature.handle(inputText, htmlSpanElement, callback)
                    return
                }
            }

            println("No match found")
            callback(null, false)

        } else {
            // it was a kotlin data type
            callback(null, false)
        }
    }

    private fun isKotlinDataType(_inputText: String): Boolean {
        return when (_inputText.replace("?", "")) {
            "Boolean",
            "Long",
            "Float",
            "Double",
            "Char",
            "Int",
            "String" -> true
            else -> false
        }
    }

    override fun getFileExtension(): String {
        return "kt"
    }


}