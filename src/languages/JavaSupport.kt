package languages

import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get


class JavaSupport : KotlinSupport() {

    companion object {
        private val REPLACE_REGEX = Regex("[^a-zA-Z._\\)\\(]+")
    }

    override fun getFileExtension(): String {
        return "java"
    }

    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (url: String?, isNewTab: Boolean) -> Unit) {
        super.getNewResourceUrl(inputText, htmlSpanElement) { newUrl, isNewTab ->
            if (newUrl == null) {
                val matchedText = getUpperSiblingsSplitted(htmlSpanElement)
                if (matchedText != null) {
                    super.getNewResourceUrl(matchedText, htmlSpanElement, callback)
                } else {
                    callback(null, false)
                }
            } else {
                callback(newUrl, isNewTab)
            }
        }
    }

    /**
     * This will return sibling element in pyramid format
     * For eg: if sibling are 1, 2, 3, 4, 5
     * this method will return 5, 54, 543, 5432, 54321 if and only if the items are clickable
     */
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
        return element.startsWith(LAYOUT_PREFIX) // can add more prefix here
    }

    override fun getImportStatement(importStatement: String): String {
        return "import $importStatement;"
    }
}
