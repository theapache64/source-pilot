package utils

object GitHubUtils {

    private const val FILE_REGEX = "https:\\/\\/github\\.com\\/.+?\\/.+?\\/.+\\..+"

    fun isFile(url: String): Boolean {
        return url.matches(FILE_REGEX)
    }
}