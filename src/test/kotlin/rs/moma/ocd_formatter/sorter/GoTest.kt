package rs.moma.ocd_formatter.sorter

class GoTest : PostFormatProcessorTest() {
    fun `test basic import block sort`() = assertEquals(
        """
        import (
        	"fmt"
        	"io"
        )
        """.trimIndent(),
        sort("file.go", """
        import (
        	"io"
        	"fmt"
        )
        """.trimIndent())
    )

    fun `test import block two groups sorted independently`() = assertEquals(
        """
        import (
        	"fmt"
        	"io"

        	"github.com/user/repo"
        )
        """.trimIndent(),
        sort("file.go", """
        import (
        	"io"
        	"fmt"

        	"github.com/user/repo"
        )
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        import (
        	"fmt"
        	"io"
        )
        """.trimIndent()
        assertEquals(input, sort("file.go", input))
    }
}