@file:Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")

package chrome.tabs

inline fun Tab(block: Tab.() -> Unit) =
        (js("{}") as Tab).apply(block)