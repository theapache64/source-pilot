package languages

import base.LanguageSupport
import extensions.startsWithUppercaseLetter
import org.w3c.dom.HTMLSpanElement
import utils.CommonParser
import utils.KotlinParser
import utils.XMLLineFinder
import kotlin.browser.window

open class KotlinSupport : LanguageSupport() {

    companion object {
        private const val DATA_BINDING_IMPORT_REGEX = ".*\\.databinding\\..+Binding"
        protected const val LAYOUT_PREFIX = ".layout."
    }

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
                val isImportStatement = htmlSpanElement.parentElement?.textContent.equals(getImportStatement(inputText))
                if (isImportStatement) {
                    inputText
                } else {
                    null
                }
            }
        }
    }

    protected open fun getImportStatement(inputText: String): String {
        return "import $inputText"
    }


    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (String?) -> Unit) {

        if (inputText.startsWith(LAYOUT_PREFIX)) {
            println("Generating new url for layout : $inputText")
            val layoutFileName = CommonParser.parseLayoutFileName(inputText)
            val currentUrl = window.location.toString()
            //https://github.com/theapache64/swipenetic/blob/master/app/src/main/java/com/theapache64/swipenetic/ui/activities/chart/ChartActivity.kt
            callback("${currentUrl.split("main")[0]}main/res/layout/$layoutFileName.xml")
        } else if (inputText.startsWith(".string.")) {
            val stringResName = KotlinParser.getStringResName(inputText)
            val currentUrl = window.location.toString()
            val stringXml = "${currentUrl.split("main")[0]}main/res/values/strings.xml"
            callback(stringXml)
            XMLLineFinder.getLineNumber(stringXml, stringResName) { lineNumber ->
                callback("$stringXml#L$lineNumber")
            }
        } else if (inputText.startsWith(".menu.")) {
            println("Generating new url for menu : $inputText")
            val menuFileName = CommonParser.parseMenuFileName(inputText)
            val currentUrl = window.location.toString()
            //https://github.com/theapache64/swipenetic/blob/master/app/src/main/java/com/theapache64/swipenetic/ui/activities/chart/ChartActivity.kt
            callback("${currentUrl.split("main")[0]}main/res/menu/$menuFileName.xml")
        } else if (imports.isNotEmpty()) {

            val currentPackageName = KotlinParser.getCurrentPackageName(getFullCode())

            // Getting possible import statements for the class
            val matchingImport = getMatchingImport(inputText, currentPackageName, htmlSpanElement)

            if (isClickableImport(matchingImport)) {
                val currentUrl = window.location.toString()
                val curFileExt = CommonParser.parseFileExt(currentUrl)
                val packageSlash = '/' + currentPackageName.replace('.', '/');
                val windowLocSplit = currentUrl.split(packageSlash)

                // Returning new url
                callback("${windowLocSplit[0]}/${matchingImport!!.replace('.', '/')}.$curFileExt#L1")
            } else {
                println("No import matched! Matching importing was : $matchingImport")
                callback(null)
            }
        } else {
            println("No imports found")
            callback(null)
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
                !isDataBindingImport(matchingImport)
    }

    private fun isDataBindingImport(matchingImport: String): Boolean {
        return matchingImport.matches(DATA_BINDING_IMPORT_REGEX)
    }

    override fun getFileExtension(): String {
        return "kt"
    }

}