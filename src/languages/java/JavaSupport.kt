package languages.java

import base.LanguageSupport
import core.BaseFeature
import languages.java.features.*
import languages.kotlin.features.*


class JavaSupport : LanguageSupport() {

    override fun getFeatures(): List<BaseFeature> {
        return listOf(
                JavaLayoutResFeature(this),
                JavaStringResFeature(this),
                JavaMenuResFeature(this),
                JavaImportStatementFeature(this),
                JavaInternalMethodCallFeature(this),
                JavaVariableFeature(this),
                JavaExternalMethodCallFeature(this)
//                JavaClassFeature(this)
        )
    }

    override fun getFileExtension(): String {
        return "java"
    }

    /*override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        super.getNewResourceUrl(inputText, htmlSpanElement) { newUrl, isNewTab ->
            if (newUrl == null) {
                val matchedText = getUpperSiblingsSplitted(htmlSpanElement)
                if (matchedText != null) {
                    super.getNewResourceUrl(matchedText, htmlSpanElemen t, callback)
                } else {
                    callback(null, false)
                }
            } else {
                callback(newUrl, isNewTab)
            }
        }
    }*/


    /**
     * This will return sibling element in pyramid format
     * For eg: if sibling are 1, 2, 3, 4, 5
     * this method will return 5, 54, 543, 5432, 54321 if and only if the items are clickable
     *//*
    private fun getUpperSiblingsSplitted(htmlSpanElement: HTMLSpanElement): String? {

        val sibSplitArr = mutableListOf<String>()
        val children = htmlSpanElement.parentElement?.childNodes
        if (children != null) {
            for (childIndex in 0..children.length) {
                val element = children[childIndex]?.textContent?.trim()?.replace(REPLACE_REGEX, "")
                if (element != null) {
                    sibSplitArr.add(element)
                }
            }
        }

        if (sibSplitArr.isNotEmpty()) {
            for (i in sibSplitArr.size - 1 downTo 0) {
                val newElement = sibSplitArr.subList(i, sibSplitArr.size).joinToString(separator = "")
                if (isSupportedElement(newElement)) {
                    return newElement
                }
            }
        }
        return null
    }

    private fun isSupportedElement(element: String): Boolean {
        return element.startsWith(".layout.") // can add more prefix here
    }
*/
    /* override fun isStringRes(htmlSpanElement: HTMLSpanElement): Boolean {
         return isRes(htmlSpanElement, "string")
     }

     override fun isMenuRes(htmlSpanElement: HTMLSpanElement): Boolean {
         return isRes(htmlSpanElement, "menu")
     }

     override fun getMenuFileName(inputText: String): String? {
         println("Menu file name is $inputText")
         return inputText
     }


     override fun isLayoutName(htmlSpanElement: HTMLSpanElement): Boolean {
         return isRes(htmlSpanElement, "layout")
     }*/

    /*private fun isRes(htmlSpanElement: HTMLSpanElement, res: String): Boolean {
        val clickedText = htmlSpanElement.textContent
        val fullLine = htmlSpanElement.parentElement?.textContent
        val semiSplit = fullLine?.split(";")?.get(0)
        if (semiSplit != null) {
            val isMatch = semiSplit.matches("R\\.$res\\.${clickedText?.replace("\\W+".toRegex(), "")}")
            println("is $clickedText is $res -> $isMatch")
            return isMatch
        }
        return true
    }*/

    /*fun getStringName(inputText: String): String {
        return JavaParser.parseStringName(inputText)
    }*/

    /*override fun getMethodRegEx(methodName: String): String {
        return "\\w+\\s+\\w+\\s+$methodName\\(\\)"
    }*/

    /*override fun getImportStatement(importStatement: String): String {
         return "import $importStatement;"
     }*/

    /*override fun isClickedOnEndClass(htmlSpanElement: HTMLSpanElement): Boolean {
        return getNextNonSpaceSiblingElement(htmlSpanElement)?.textContent?.trim() == ";"
    }*/
}
