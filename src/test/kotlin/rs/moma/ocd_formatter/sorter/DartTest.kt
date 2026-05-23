package rs.moma.ocd_formatter.sorter

class DartTest : PostFormatProcessorTest() {
    fun `test basic import sort`() = assertEquals(
        """
        import 'package:flutter/material.dart';
        import 'package:dio/dio.dart';
        """.trimIndent(),
        sort("file.dart", """
        import 'package:dio/dio.dart';
        import 'package:flutter/material.dart';
        """.trimIndent())
    )

    fun `test part sort`() = assertEquals(
        """
        part 'src/my_library.dart';
        part 'src/foo.dart';
        """.trimIndent(),
        sort("file.dart", """
        part 'src/foo.dart';
        part 'src/my_library.dart';
        """.trimIndent())
    )

    fun `test already sorted unchanged`() {
        val input = """
        import 'package:flutter/material.dart';
        import 'package:dio/dio.dart';
        """.trimIndent()
        assertEquals(input, sort("file.dart", input))
    }
}