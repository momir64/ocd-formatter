package rs.moma.ocd_formatter.sorter

class CSharpTest : PostFormatProcessorTest() {
    fun `test basic using sort`() = assertEquals(
        """
        using System.Collections.Generic;
        using System;
        """.trimIndent(),
        sort("file.cs", """
        using System;
        using System.Collections.Generic;
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        using System.Collections.Generic;
        using System;
        """.trimIndent()
        assertEquals(input, sort("file.cs", input))
    }
}