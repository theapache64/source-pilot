package languages.xml

import base.LanguageSupport
import core.BaseFeature
import languages.xml.features.MiscFeatures
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.XMLLineFinder
import kotlin.browser.window

/**
 * This class is used to support Android XML
 */
class AndroidXMLSupport : LanguageSupport() {
    override fun getFeatures(): List<BaseFeature> {
        return listOf(
                MiscFeatures(this)
        )
    }

    override fun getFileExtension(): String {
        return "xml"
    }
}
