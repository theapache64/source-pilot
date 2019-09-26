import extensions.startsWithUppercaseLetter
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.asList
import org.w3c.xhr.XMLHttpRequest
import utils.Parser
import kotlin.browser.document
import kotlin.browser.window


lateinit var fullCode: String
lateinit var imports: List<String>
lateinit var currentFilePath: String
var isControlActive = false;
var activeElement: HTMLSpanElement? = null
var resLink: String? = null


fun checkIsClickable(inputText: String) {

    println("Checking if $inputText is clickable...")

    if (isLayoutFile(inputText)) {

    } else {

        if (imports.isNotEmpty()) {

            val matchingImports = imports.filter { it.endsWith(".${inputText}") }
            println("Matching imports are")
            println(matchingImports)
            val currentPackageName = Parser.currentPackageName(fullCode)
            val matchingImport = if (matchingImports.isNotEmpty()) {
                matchingImports.first()
            } else {
                println("No import matched for $inputText, setting current name : $currentPackageName")
                if (inputText.startsWithUppercaseLetter()) {
                    "$currentPackageName.$inputText"
                } else {
                    null
                }
            }

            if (matchingImport != null &&
                    !matchingImport.startsWith("android.") &&
                    !matchingImport.startsWith("androidx.")
            ) {
                println("Matching import is $matchingImport")
                println("Current package name $currentPackageName")
                val currentUrl = window.location.toString()
                println("currentUrl is $currentUrl")
                val curFileExt = Parser.parseFileExt(currentUrl)
                val packageSlash = '/' + currentPackageName.replace('.', '/');
                val windowLocSplit = currentUrl.split(packageSlash)
                val newUrl = windowLocSplit[0] + '/' + matchingImport.replace('.', '/') + '.' + curFileExt + "#L1"
                println("New url is $newUrl")
                resLink = newUrl
                activeElement?.style?.textDecoration = "underline"
                doubleCheckUrl(newUrl)
            } else {
                println("No import matched! Matching importing was : $matchingImport")
            }

        }
    }


}

fun isLayoutFile(inputText: String): Boolean {
    // TODO :
    return false
}

fun doubleCheckUrl(newUrl: String) {

    val xhr = XMLHttpRequest()
    xhr.open("GET", newUrl)
    xhr.onload = {
        if (xhr.status.toInt() != 200) {
            // valid
            println("Invalid URL : $newUrl")
            resLink = null
            activeElement?.style?.textDecoration = "none"
        } else {
            println("Valid URL it was :)")
        }
    }
    xhr.send()
}

fun removeUnderlineFromActiveElement() {
    activeElement?.style?.textDecoration = "none"
    activeElement = null
}

fun underlineActiveElement() {
    activeElement?.let { activeElement ->
        val className = activeElement.innerText
        if (isControlActive) {
            checkIsClickable(className)
        }
    }
}


fun main() {

    println("Source Pilot Activated (y)")
    fullCode = document.querySelector("table.highlight tbody")?.textContent ?: ""
    imports = Parser.parseImports(fullCode)
    println("Imports are $imports")
    currentFilePath = getCurrentFilePath()

    // Element Mouse Over
    val allSpan = document.querySelectorAll("table.highlight tbody tr td.blob-code span")
    allSpan.asList().forEach { _node ->
        val node = _node as HTMLSpanElement

        // Mouse over span
        node.onmouseover = {
            activeElement = node
            println("Mouse over...")
            underlineActiveElement()
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
            underlineActiveElement()
        }
    }

    document.onkeyup = {
        if (it.which == 17) {
            isControlActive = false
            removeUnderlineFromActiveElement()
        }
    }

}

fun getCurrentFilePath(): String {
    val currentUrl = window.location.toString()
    return Parser.getCurrentFilePath(currentUrl)
}
