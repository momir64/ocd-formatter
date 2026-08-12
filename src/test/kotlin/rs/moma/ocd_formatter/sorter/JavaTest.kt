package rs.moma.ocd_formatter.sorter

class JavaTest : PostFormatProcessorTest() {
    fun `test basic import sort`() = assertEquals(
        """
        import com.example.LongClassName;
        import com.example.Abc;
        """.trimIndent(),
        sort("file.java", """
        import com.example.Abc;
        import com.example.LongClassName;
        """.trimIndent())
    )

    fun `test wildcard sorts after specific of same length`() = assertEquals(
        """
        import ab.cd.ef.gh.Z;
        import ab.cd.ef.gh.*;
        """.trimIndent(),
        sort("file.java", """
        import ab.cd.ef.gh.*;
        import ab.cd.ef.gh.Z;
        """.trimIndent())
    )

    fun `test wildcard sorts after even with earlier alphabetical difference`() = assertEquals(
        """
        import bb.aaaa.Z;
        import aa.zzzz.*;
        """.trimIndent(),
        sort("file.java", """
        import aa.zzzz.*;
        import bb.aaaa.Z;
        """.trimIndent())
    )
}