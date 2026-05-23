package rs.moma.ocd_formatter.sorter

class LuaTest : PostFormatProcessorTest() {
    fun `test basic require sort`() = assertEquals(
        """
        require("socket")
        require("io")
        """.trimIndent(),
        sort("file.lua", """
        require("io")
        require("socket")
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        require("socket")
        require("io")
        """.trimIndent()
        assertEquals(input, sort("file.lua", input))
    }
}