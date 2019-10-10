package languages.kotlin.features

import base.LanguageSupport
import core.BaseFeature
import languages.KotlinSupport
import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.KotlinParser
import kotlin.browser.window

class ImportStatementFeature : BaseFeature {

    companion object {
        private const val DATA_BINDING_IMPORT_REGEX = ".*\\.databinding\\..+Binding"
    }

    override fun isMatch(inputText: String, htmlSpanElement: HTMLSpanElement): Boolean {
        val fullLine = htmlSpanElement.parentElement?.textContent ?: ""
        return KotlinParser.IMPORT_PATTERN.matches(fullLine)
    }


    override fun handle(languageSupport: LanguageSupport, inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        println("Clicked on an import statement")

        val currentPackageName = KotlinParser.getCurrentPackageName(languageSupport.getFullCode())
        val importStatement = htmlSpanElement.parentElement?.textContent!!

        val isDir: Boolean
        val importPackage = if (isClickedOnEndClass(htmlSpanElement)) {
            isDir = false
            KotlinParser.parseImportPackage(importStatement)
        } else {
            // directory navigation
            isDir = true
            getDirectoryPackage(htmlSpanElement).trim()
        }

        if (isClickableImport(importPackage)) {
            gotoImport(languageSupport as KotlinSupport, currentPackageName, importPackage, isDir, callback)
        } else {
            callback(null, false)
        }
    }


    /**
     * To check if the passed import passes all non matching conditions
     */
    private fun isClickableImport(matchingImport: String?): Boolean {
        return matchingImport != null &&
                !matchingImport.startsWith("android.") &&
                !matchingImport.startsWith("java.") &&
                !matchingImport.startsWith("androidx.") &&
                !matchingImport.startsWith("kotlinx.android.synthetic.") &&
                !matchingImport.startsWith("com.google.android.material.") &&
                !isDataBindingImport(matchingImport)
    }

    private fun isDataBindingImport(matchingImport: String): Boolean {
        return matchingImport.matches(DATA_BINDING_IMPORT_REGEX)
    }

    private fun isClickedOnEndClass(htmlSpanElement: HTMLSpanElement): Boolean {
        return htmlSpanElement.nextElementSibling == null
    }


    private fun getDirectoryPackage(htmlSpanElement: HTMLSpanElement): String {
        var s = ""
        var x: Element? = htmlSpanElement
        while (x != null) {
            val text = x.textContent
            if (text != null && text.trim() != "import") {
                s = "$text$s"
            }
            x = x.previousElementSibling
        }
        return s
    }

    private fun gotoImport(kotlinSupport: KotlinSupport, currentPackageName: String, matchingImport: String?, isDir: Boolean, callback: (url: String?, isNewTab: Boolean) -> Unit, lineNumber: Int = 1) {

        val currentUrl = window.location.toString()
        val curFileExt = CommonParser.parseFileExt(currentUrl)
        val packageSlash = '/' + currentPackageName.replace('.', '/');
        val windowLocSplit = currentUrl.split(packageSlash)
        val fileExt = if (isDir) {
            ""
        } else {
            ".$curFileExt#L$lineNumber"
        }

        // Returning new url
        kotlinSupport.fileUrl = "${windowLocSplit[0]}/${matchingImport!!.replace('.', '/')}$fileExt"
        callback(kotlinSupport.fileUrl, true)
    }


}