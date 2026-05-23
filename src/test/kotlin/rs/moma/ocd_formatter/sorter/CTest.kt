package rs.moma.ocd_formatter.sorter

class CTest : PostFormatProcessorTest() {
    fun `test basic include sort`() = assertEquals(
        """
        #include <stdlib.h>
        #include <stdio.h>
        """.trimIndent(),
        sort("file.c", """
        #include <stdio.h>
        #include <stdlib.h>
        """.trimIndent())
    )

    fun `test h file sort`() = assertEquals(
        """
        #include <stdlib.h>
        #include <stdio.h>
        """.trimIndent(),
        sort("file.h", """
        #include <stdio.h>
        #include <stdlib.h>
        """.trimIndent())
    )

    fun `test cpp file sort`() = assertEquals(
        """
        #include <iostream>
        #include <string>
        """.trimIndent(),
        sort("file.cpp", """
        #include <string>
        #include <iostream>
        """.trimIndent())
    )

    fun `test hpp file sort`() = assertEquals(
        """
        #include <iostream>
        #include <string>
        """.trimIndent(),
        sort("file.hpp", """
        #include <string>
        #include <iostream>
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        #include <iostream>
        #include <string>
        """.trimIndent()
        assertEquals(input, sort("file.cpp", input))
    }
}