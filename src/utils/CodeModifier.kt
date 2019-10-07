package utils

import org.w3c.dom.Element
import org.w3c.dom.get
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
                        val textContent = node.textContent
                        sb.append("<span>$textContent</span>")
                    }
                }
            }

            if (sb.isNotEmpty()) {
                td.innerHTML = sb.toString()
            }
        }
    }

}