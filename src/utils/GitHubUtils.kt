package utils

object GitHubUtils {

    private const val FILE_REGEX = "https:\\/\\/github\\.com\\/.+?\\/.+?\\/.+\\..+"

    fun isFile(url: String): Boolean {
        return url.matches(FILE_REGEX)
    }

    fun toRAWUrl(fileUrl: String): String {
        return fileUrl.replace("https://github.com", "https://raw.githubusercontent.com").replaceFirst("/blob", "")
    }
}