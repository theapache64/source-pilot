package utils

object JavaParser {
    private val STRING_NAME_REGEX = "\\.?(\\w+)".toRegex()
    fun parseStringName(inputText: String): String {
        println("Parsing $inputText")
        return STRING_NAME_REGEX.find(inputText)!!.groups[1]!!.value
    }

}