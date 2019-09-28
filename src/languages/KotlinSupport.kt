package languages

import base.LanguageSupport
import extensions.startsWithUppercaseLetter
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.KotlinParser
import kotlin.browser.window

open class KotlinSupport : LanguageSupport() {

    private val imports by lazy { KotlinParser.parseImports(getFullCode()) }

    /**
     * To get matching import for the input passed from the imports in the file.
     */
    private fun getMatchingImport(inputText: String, currentPackageName: String, htmlSpanElement: HTMLSpanElement): String? {

        val matchingImports = imports.filter { it.endsWith(".${inputText}") }
        println("Matching imports are : $matchingImports")
        return if (matchingImports.isNotEmpty()) {
            matchingImports.first()
        } else {
            println("No import matched for $inputText, setting current name : $currentPackageName")
            if (inputText.startsWithUppercaseLetter()) {
                "$currentPackageName.$inputText"
            } else {
                // Checking if it's the import statement it self
                println("Checking if it's import statement")
                val isImportStatement = htmlSpanElement.parentElement?.textContent.equals("import $inputText")
                if (isImportStatement) {
                    inputText
                } else {
                    null
                }
            }
        }
    }

    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement): String? {

        if (inputText.startsWith(".layout.")) {
            println("Generating new url for layout : $inputText")
            val layoutFileName = CommonParser.parseLayoutFileName(inputText)
            val currentUrl = window.location.toString()
            //https://github.com/theapache64/swipenetic/blob/master/app/src/main/java/com/theapache64/swipenetic/ui/activities/chart/ChartActivity.kt
            return "${currentUrl.split("java")[0]}/res/layout/$layoutFileName.xml"
        } else if (imports.isNotEmpty()) {

            val currentPackageName = KotlinParser.currentPackageName(getFullCode())

            // Getting possible import statements for the class
            val matchingImport = getMatchingImport(inputText, currentPackageName, htmlSpanElement)

            if (isClickableImport(matchingImport)) {
                val currentUrl = window.location.toString()
                val curFileExt = CommonParser.parseFileExt(currentUrl)
                val packageSlash = '/' + currentPackageName.replace('.', '/');
                val windowLocSplit = currentUrl.split(packageSlash)

                // Returning new url
                return "${windowLocSplit[0]}/${matchingImport!!.replace('.', '/')}.$curFileExt#L1"
            } else {
                println("No import matched! Matching importing was : $matchingImport")
            }
        } else {
            println("No imports found")
        }


        return null
    }

    /**
     * To check if the passed import passes all non matching conditions
     */
    private fun isClickableImport(matchingImport: String?): Boolean {
        return matchingImport != null &&
                !matchingImport.startsWith("android.") &&
                !matchingImport.startsWith("androidx.")
    }

    override fun getFileExtension(): String {
        return "kt"
    }

}