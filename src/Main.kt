import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.asList
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

    println("✈ source-pilot activated")

    // Changing non span elements to span elements
    val codeTable = document.querySelector("table.highlight")!!
    val newCode = CodeModifier.getSpanWrapped(codeTable)
    codeTable.innerHTML = newCode

    // Element Mouse Over ib
    val allCodeSpan = document.querySelectorAll("table.highlight tbody tr td.blob-code span")
    allCodeSpan.asList().forEach { _node ->
        val node = _node as HTMLSpanElement

        // Mouse over span
        node.onmouseover = {
            activeElement = node
            println("Mouse over... : ${node.innerText}")
            checkIfClickable()
        }

        // Mouse leave span
        node.onmouseleave = {
            println("Mouse left from ${node.innerText}, Removing underline...")
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
    println("Checking if $inputText is clickable...")
    val newUrl = support.getNewResourceUrl(inputText, activeElement!!)
    if (newUrl != null) {
        println("New url is $newUrl")
        resLink = newUrl
        activeElement?.style?.textDecoration = "underline"
        doubleCheckUrl(newUrl)
    }
}


fun doubleCheckUrl(newUrl: String) {
    val xhr = XMLHttpRequest()
    xhr.open("GET", newUrl)
    xhr.onload = {
        if (xhr.status.toInt() != 200) {
            // invalid
            println("Invalid URL : $newUrl")
            resLink = null
            activeElement?.style?.textDecoration = "none"
        } else {
            // valid
            println("Valid URL it was :)")
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