package rs.moma.ocd_formatter.sorter

class UnknownTest : PostFormatProcessorTest() {
    fun `test unknown extension unchanged`() {
        val input = """
        import os
        import numpy
        """.trimIndent()
        assertEquals(input, sort("file.txt", input))
    }

    fun `test no extension unchanged`() {
        val input = """
        import os
        import numpy
        """.trimIndent()
        assertEquals(input, sort("Makefile", input))
    }
}