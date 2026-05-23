package rs.moma.ocd_formatter.sorter

class PhpTest : PostFormatProcessorTest() {
    fun `test basic use sort`() = assertEquals(
        """
        use App\Http\Controllers\Controller;
        use App\Models\User;
        """.trimIndent(),
        sort("file.php", """
        use App\Models\User;
        use App\Http\Controllers\Controller;
        """.trimIndent())
    )

    fun `test require sort`() = assertEquals(
        """
        require 'vendor/autoload.php';
        require 'config.php';
        """.trimIndent(),
        sort("file.php", """
        require 'config.php';
        require 'vendor/autoload.php';
        """.trimIndent())
    )

    fun `test include sort`() = assertEquals(
        """
        include 'templates/header.php';
        include 'config.php';
        """.trimIndent(),
        sort("file.php", """
        include 'config.php';
        include 'templates/header.php';
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        use App\Http\Controllers\Controller;
        use App\Models\User;
        """.trimIndent()
        assertEquals(input, sort("file.php", input))
    }
}