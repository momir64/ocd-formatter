package rs.moma.ocd_formatter.sorter

class RustTest : PostFormatProcessorTest() {
    fun `test basic use sort`() = assertEquals(
        """
        use std::collections::HashMap;
        use std::io;
        """.trimIndent(),
        sort("file.rs", """
        use std::io;
        use std::collections::HashMap;
        """.trimIndent())
    )

    fun `test multiple groups`() = assertEquals(
        """
        use std::collections::HashMap;
        use std::io;

        use crate::other::Long;
        use crate::Aa;
        """.trimIndent(),
        sort("file.rs", """
        use std::io;
        use std::collections::HashMap;

        use crate::Aa;
        use crate::other::Long;
        """.trimIndent())
    )
}