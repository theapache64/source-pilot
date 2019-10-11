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

    override fun getFileExtension(): String {
        return "kt"
    }


}