package chrome.tabs


@Suppress("UNCHECKED_CAST_TO_NATIVE_INTERFACE")
inline fun Tab(block: Tab.() -> Unit) =
        (js("{}") as Tab).apply(block)