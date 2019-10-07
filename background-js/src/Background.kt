import chrome.tabs.Tab

fun main() {
    chrome.runtime.onInstalled.addListener {
        console.log("Installed yeah!!")
        val tab = Tab {
            url = "https://github.com/theapache64/source-pilot/blob/master/WELCOME.md#ahoy"
        }
        chrome.tabs.create(tab) {
            println("Tab created ${it.url}")
        }
    }
}

