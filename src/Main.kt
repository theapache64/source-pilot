import org.w3c.dom.*
import org.w3c.xhr.XMLHttpRequest
import utils.CodeModifier
import utils.SupportManager
import kotlin.browser.document
import kotlin.browser.window

var isControlActive = false;
var activeElement: HTMLSpanElement? = null
var resLink: String? = null
val supportManager = SupportManager()
val support = supportManager.getSupportForCurrentFile()

fun main() {
    activateSourcePilot()
}


private fun activateSourcePilot() {

    println("✈ source-pilot activated")

    // Changing non span elements to span elements
    val codeTable = document.querySelector("table.highlight")!!
    val newCode = CodeModifier.getSpanWrapped(codeTable)
    codeTable.innerHTML = newCode

    // Element Mouse Over ib
    val allCodeSpan = document.querySelectorAll("table.highlight tbody tr td.blob-code > span")
    allCodeSpan.asList().forEach { _node ->
        val node = _node as HTMLSpanElement

        // Mouse over span
        node.onmouseover = {
            activeElement = node
            checkIfClickable()
        }

        // Mouse leave span
        node.onmouseleave = {
            removeUnderlineFromActiveElement()
        }

        // Mouse click
        node.onclick = {
            if (isControlActive && resLink != null) {
                window.open(resLink!!, "_blank")
            }
        }
    }

    document.onkeydown = {
        if (it.which == 17) {
            isControlActive = true
            checkIfClickable()
        }
    }

    document.onkeyup = {
        if (it.which == 17) {
            isControlActive = false
            removeUnderlineFromActiveElement()
        }
    }
}

fun checkIsClickable(inputText: String) {
    support.getNewResourceUrl(inputText, activeElement!!) { newUrl ->
        if (newUrl != null) {
            resLink = newUrl
            activeElement?.style?.textDecoration = "underline"
            doubleCheckUrl(newUrl)
        } else {
            println("New url is $newUrl, so it's not clickable :(")
            resLink = null
        }
    }

}


fun doubleCheckUrl(newUrl: String) {
    val xhr = XMLHttpRequest()
    xhr.open("GET", newUrl)
    xhr.onload = {
        if (xhr.status.toInt() != 200) {
            // invalid
            resLink = null
            activeElement?.style?.textDecoration = "none"
        }
    }
    xhr.send()
}

fun removeUnderlineFromActiveElement() {
    activeElement?.style?.textDecoration = "none"
    activeElement = null
}

fun checkIfClickable() {
    activeElement?.let { activeElement ->
        val inputText = activeElement.innerText
        if (isControlActive) {
            checkIsClickable(inputText)
        }
    }
}