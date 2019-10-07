import chrome.tabs.Tab

fun main() {
    chrome.runtime.onInstalled.addListener {
        console.log("Installed yeah!!")
        val tab = Tab {
            url = "https://google.com"
        }
        chrome.tabs.create(tab) {
            println("Tab created ${it.url}")
        }
    }
}

