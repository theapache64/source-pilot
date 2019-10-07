@file:JsQualifier("chrome.runtime")

package chrome.runtime

external val onInstalled: Installed

external interface Installed {
    fun addListener(callback: (Any) -> Unit)
}