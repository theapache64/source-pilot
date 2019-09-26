package extensions

fun String.startsWithUppercaseLetter(): Boolean {
    return this.matches(Regex("[A-Z]{1}.*"))
}
