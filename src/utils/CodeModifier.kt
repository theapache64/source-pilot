package utils

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

object CodeModifier {

    private val SPAN_WRAP_REGEX = Regex("<\\/span>\\s*([^\\s]+?)(?:<span|<\\/td>)")

    fun getSpanWrapped(codeTable: Element): String {
        var tbodyHtml = codeTable.innerHTML
        val matches = SPAN_WRAP_REGEX.findAll(tbodyHtml)
        matches.forEach { match ->
            val fullMatch = match.groups[0]!!.value
            val g1 = match.groups[1]!!.value
            val g1m = "<span>$g1</span>"
            val newMatch = fullMatch.replace(g1, g1m)
            tbodyHtml = tbodyHtml.replace(fullMatch, newMatch)
        }

        return tbodyHtml
    }

}