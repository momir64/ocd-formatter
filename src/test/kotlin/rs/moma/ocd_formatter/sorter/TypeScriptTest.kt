package rs.moma.ocd_formatter.sorter

class TypeScriptTest : PostFormatProcessorTest() {
    fun `test ts import sort`() = assertEquals(
        """
        import { Component } from '@angular/core';
        import React from 'react';
        """.trimIndent(),
        sort("file.ts", """
        import React from 'react';
        import { Component } from '@angular/core';
        """.trimIndent())
    )

    fun `test tsx import sort`() = assertEquals(
        """
        import { Component } from '@angular/core';
        import React from 'react';
        """.trimIndent(),
        sort("file.tsx", """
        import React from 'react';
        import { Component } from '@angular/core';
        """.trimIndent())
    )

    fun `test jsx import sort`() = assertEquals(
        """
        import { Component } from '@angular/core';
        import React from 'react';
        """.trimIndent(),
        sort("file.jsx", """
        import React from 'react';
        import { Component } from '@angular/core';
        """.trimIndent())
    )
}