package languages


class JavaSupport : KotlinSupport() {

    override fun getFileExtension(): String {
        return "java"
    }

    override fun getImportStatement(inputText: String): String {
        return "import $inputText;"
    }
}
