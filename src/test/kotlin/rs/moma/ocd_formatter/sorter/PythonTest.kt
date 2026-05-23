package rs.moma.ocd_formatter.sorter

class PythonTest : PostFormatProcessorTest() {
    fun `test single import unchanged`() = assertEquals(
        "import os",
        sort("file.py", "import os")
    )

    fun `test two imports sorted by length descending`() = assertEquals(
        """
        import numpy
        import os
        """.trimIndent(),
        sort("file.py", """
        import os
        import numpy
        """.trimIndent())
    )

    fun `test same length imports sorted alphabetically`() = assertEquals(
        """
        import io
        import os
        """.trimIndent(),
        sort("file.py", """
        import os
        import io
        """.trimIndent())
    )

    fun `test from import sorts with regular import by length`() = assertEquals(
        """
        from collections import defaultdict
        import os
        """.trimIndent(),
        sort("file.py", """
        import os
        from collections import defaultdict
        """.trimIndent())
    )

    fun `test two groups sorted independently`() = assertEquals(
        """
        import numpy
        import os

        import io
        import re
        """.trimIndent(),
        sort("file.py", """
        import os
        import numpy

        import re
        import io
        """.trimIndent())
    )

    fun `test imports followed by code`() = assertEquals(
        """
        import numpy
        import os

        def main():
            pass
        """.trimIndent(),
        sort("file.py", """
        import os
        import numpy

        def main():
            pass
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        import numpy
        import os
        """.trimIndent()
        assertEquals(input, sort("file.py", input))
    }

    fun `test no imports unchanged`() {
        val input = """
        def main():
            pass
        """.trimIndent()
        assertEquals(input, sort("file.py", input))
    }

    fun `test empty file unchanged`() = assertEquals("", sort("file.py", ""))

    fun `test multiline with trailing comma`() = assertEquals(
        """
        from sklearn.naive_bayes import (
            MultinomialNB,
            ComplementNB,
        )
        """.trimIndent(),
        sort("file.py", """
        from sklearn.naive_bayes import (
            ComplementNB,
            MultinomialNB,
        )
        """.trimIndent())
    )

    fun `test multiline without trailing comma`() = assertEquals(
        """
        from sklearn.naive_bayes import (
            MultinomialNB,
            ComplementNB
        )
        """.trimIndent(),
        sort("file.py", """
        from sklearn.naive_bayes import (
            ComplementNB,
            MultinomialNB
        )
        """.trimIndent())
    )

    fun `test multiline with blank lines inside`() = assertEquals(
        """
        from sklearn.naive_bayes import (
            MultinomialNB,
            ComplementNB
        )
        """.trimIndent(),
        sort("file.py", """
        from sklearn.naive_bayes import (
            ComplementNB,


            MultinomialNB


        )
        """.trimIndent())
    )

    fun `test multiline sorts before single line by total length`() = assertEquals(
        """
        from sklearn.naive_bayes import (
            MultinomialNB,
            ComplementNB,
        )
        import os
        """.trimIndent(),
        sort("file.py", """
        import os
        from sklearn.naive_bayes import (
            ComplementNB,
            MultinomialNB,
        )
        """.trimIndent())
    )
}