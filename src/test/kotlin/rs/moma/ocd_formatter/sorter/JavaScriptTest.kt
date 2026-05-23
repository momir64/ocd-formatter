package rs.moma.ocd_formatter.sorter

class JavaScriptTest : PostFormatProcessorTest() {
    fun `test import sort`() = assertEquals(
        """
        import { something } from 'somewhere';
        import React from 'react';
        """.trimIndent(),
        sort("file.js", """
        import React from 'react';
        import { something } from 'somewhere';
        """.trimIndent())
    )

    fun `test require sort`() = assertEquals(
        """
        const express = require('express');
        const fs = require('fs');
        """.trimIndent(),
        sort("file.js", """
        const fs = require('fs');
        const express = require('express');
        """.trimIndent())
    )

    fun `test let require sort`() = assertEquals(
        """
        let express = require('express');
        let fs = require('fs');
        """.trimIndent(),
        sort("file.js", """
        let fs = require('fs');
        let express = require('express');
        """.trimIndent())
    )

    fun `test var require sort`() = assertEquals(
        """
        var express = require('express');
        var fs = require('fs');
        """.trimIndent(),
        sort("file.js", """
        var fs = require('fs');
        var express = require('express');
        """.trimIndent())
    )

    fun `test non-require const not sorted with imports`() {
        val input = """
        import React from 'react';

        const x = 1;
        """.trimIndent()
        assertEquals(input, sort("file.js", input))
    }
}