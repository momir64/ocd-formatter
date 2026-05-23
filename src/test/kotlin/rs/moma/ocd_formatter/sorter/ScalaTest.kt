package rs.moma.ocd_formatter.sorter

class ScalaTest : PostFormatProcessorTest() {
    fun `test basic import sort`() = assertEquals(
        """
        import scala.collection.mutable.HashMap
        import scala.collection.List
        """.trimIndent(),
        sort("file.scala", """
        import scala.collection.List
        import scala.collection.mutable.HashMap
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        import scala.collection.mutable.HashMap
        import scala.collection.List
        """.trimIndent()
        assertEquals(input, sort("file.scala", input))
    }
}