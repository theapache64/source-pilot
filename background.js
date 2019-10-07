chrome.runtime.onInstalled.addListener(function (object) {
    chrome.tabs.create({url: "https://github.com/theapache64/source-pilot/blob/master/WELCOME.md#source-pilot"}, function (tab) {
        console.log("New tab launched with http://yoursite.com/");
    });
});