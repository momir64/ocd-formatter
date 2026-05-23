package rs.moma.ocd_formatter.sorter

class SwiftTest : PostFormatProcessorTest() {
    fun `test basic import sort`() = assertEquals(
        """
        import Foundation
        import Combine
        """.trimIndent(),
        sort("file.swift", """
        import Combine
        import Foundation
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        import Foundation
        import Combine
        """.trimIndent()
        assertEquals(input, sort("file.swift", input))
    }
}