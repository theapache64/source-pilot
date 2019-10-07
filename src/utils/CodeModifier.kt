package utils

import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.isElement

object CodeModifier {

    /**
     * To add span to text elements (non-span elements) that are children of code table.
     */
    fun spanNonSpanned() {
        val tdBlobCodes = document.querySelectorAll("table.highlight tbody tr td.blob-code")
        for (tdIndex in 0 until tdBlobCodes.length) {
            val td = tdBlobCodes.get(tdIndex) as Element
            val sb = StringBuilder()
            for (nodeIndex in 0 until td.childNodes.length) {
                val node = td.childNodes[nodeIndex]
                if (node != null) {
                    if (node.isElement) {
                        val x = node as Element
                        sb.append(x.outerHTML)
                    } else {
                        // text element, converting it to span
                        val textContent = node.textContent
                        if (textContent != null) {
                            if (textContent.startsWith(" ")) {
                                // two span needed
                                val spaceAndContent = parseSpaceAndContent(textContent)
                                if (spaceAndContent != null) {
                                    sb.append("<span>${spaceAndContent.first}</span>")
                                    sb.append("<span>${spaceAndContent.second}</span>")
                                } else {
                                    sb.append("<span>$textContent</span>")
                                }
                            } else {
                                sb.append("<span>$textContent</span>")
                            }
                        }
                    }
                }
            }

            if (sb.isNotEmpty()) {
                td.innerHTML = sb.toString()
            }
        }
    }

    private val SPACE_AND_CONTENT_REGEX = "(?<spaces>\\s*)(?<content>.+)".toRegex()
    private fun parseSpaceAndContent(textContent: String): Pair<String, String>? {
        val result = SPACE_AND_CONTENT_REGEX.find(textContent)
        if (result != null) {
            val groups = result.groups
            return Pair(groups[1]!!.value, groups[2]!!.value)
        }
        return null
    }

    fun splitDotSpanned() {
        val allSpans = document.querySelectorAll("table.highlight tbody tr td.blob-code > span")
        allSpans.asList().forEach { _span ->
            val span = _span as HTMLSpanElement
            val spanClass = span.className
            span.textContent?.let { spanText ->
                if (spanText.contains(".")) {
                    val sb = toString()
                    val dotSplit = sb.split(".")
                    val spanParent = span.parentElement as HTMLElement
                    //TODO:
                    span.remove()
                }
            }

        }
    }

}