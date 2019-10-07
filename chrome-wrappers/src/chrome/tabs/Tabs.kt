@file:JsQualifier("chrome.tabs")

package chrome.tabs

external fun create(tab: Tab, callback: (Tab) -> Unit)

external interface Tab {
    var url: String?
}
