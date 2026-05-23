package rs.moma.ocd_formatter.sorter

class KotlinTest : PostFormatProcessorTest() {
    fun `test basic import sort`() = assertEquals(
        """
        import com.example.LongClassName
        import com.example.Abc
        """.trimIndent(),
        sort("file.kt", """
        import com.example.Abc
        import com.example.LongClassName
        """.trimIndent())
    )

    fun `test wildcard sorts after specific of same length`() = assertEquals(
        """
        import ab.cd.ef.gh.Z
        import ab.cd.ef.gh.*
        """.trimIndent(),
        sort("file.kt", """
        import ab.cd.ef.gh.*
        import ab.cd.ef.gh.Z
        """.trimIndent())
    )

    fun `test multiple groups`() = assertEquals(
        """
        import com.example.Long
        import com.example.Ab

        import org.other.Thing
        import org.other.Aa
        """.trimIndent(),
        sort("file.kt", """
        import com.example.Ab
        import com.example.Long

        import org.other.Aa
        import org.other.Thing
        """.trimIndent())
    )

    fun `test non-import lines not sorted`() = assertEquals(
        """
        import com.example.Long
        import com.example.Ab

        class Foo
        """.trimIndent(),
        sort("file.kt", """
        import com.example.Ab
        import com.example.Long

        class Foo
        """.trimIndent())
    )
}