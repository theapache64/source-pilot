package languages

import org.w3c.dom.Element
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.get


class JavaSupport : KotlinSupport() {

    companion object {
        private val REPLACE_REGEX = Regex("[^a-zA-Z._\\)\\(]+")
    }

    override fun getFileExtension(): String {
        return "java"
    }

    override fun getNewResourceUrl(inputText: String, htmlSpanElement: HTMLSpanElement, callback: (String?) -> Unit) {
        super.getNewResourceUrl(inputText, htmlSpanElement) { newUrl ->
            if (newUrl == null) {
                val matchedText = getUpperSiblingsSplitted(htmlSpanElement)
                if (matchedText != null) {
                    super.getNewResourceUrl(matchedText, htmlSpanElement, callback)
                } else {
                    callback(null)
                }
            } else {
                callback(newUrl)
            }
        }
    }

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
        val newArr = mutableListOf<String>()
        for (i in sibSplitArr.size - 1 downTo 0) {
            console.log("I is ", i)
            val newElement = sibSplitArr.subList(i, sibSplitArr.size).joinToString(separator = "")
            if (isSupportedElement(newElement)) {
                return newElement
            }
        }
        return null
    }

    private fun isSupportedElement(element: String): Boolean {
        return element.startsWith(LAYOUT_PREFIX) // can add more prefix here
    }

    override fun getImportStatement(inputText: String): String {
        return "import $inputText;"
    }
}
