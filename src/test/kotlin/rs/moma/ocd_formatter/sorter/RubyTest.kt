package rs.moma.ocd_formatter.sorter

class RubyTest : PostFormatProcessorTest() {
    fun `test basic require sort`() = assertEquals(
        """
        require 'active_record'
        require 'json'
        """.trimIndent(),
        sort("file.rb", """
        require 'json'
        require 'active_record'
        """.trimIndent())
    )

    fun `test require_relative sort`() = assertEquals(
        """
        require_relative 'models/user'
        require_relative 'lib/foo'
        """.trimIndent(),
        sort("file.rb", """
        require_relative 'lib/foo'
        require_relative 'models/user'
        """.trimIndent())
    )

    fun `test require and require_relative sorted together`() = assertEquals(
        """
        require_relative 'models/user'
        require 'active_record'
        require 'json'
        """.trimIndent(),
        sort("file.rb", """
        require 'json'
        require 'active_record'
        require_relative 'models/user'
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        require 'active_record'
        require 'json'
        """.trimIndent()
        assertEquals(input, sort("file.rb", input))
    }
}