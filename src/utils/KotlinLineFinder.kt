package utils

import org.w3c.xhr.XMLHttpRequest

object KotlinLineFinder {

    /**
     * To get line number
     */
    fun getLineNumber(fileUrl: String, regEx: String, callback: (Int) -> Unit) {
        val rawUrl = GitHubUtils.toRAWUrl(fileUrl)
        val xhr = XMLHttpRequest()
        xhr.open("GET", rawUrl)
        xhr.onreadystatechange = {
            if (xhr.readyState.toInt() == 4 && xhr.status.toInt() == 200) {
                val lineNumber = getLineNumber(xhr.responseText, regEx)
                callback(lineNumber)
            }
        };
        xhr.send()
    }

    private fun getLineNumber(fileData: String, regEx: String): Int {
        val lines = fileData.split("\n")
        lines.forEachIndexed { index, line ->
            if (line.matches(regEx)) {
                return index + 1
            }
        }
        return -1
    }
}