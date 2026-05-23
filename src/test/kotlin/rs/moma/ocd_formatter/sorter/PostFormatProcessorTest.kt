package rs.moma.ocd_formatter.sorter

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.application.options.CodeStyle
import com.intellij.openapi.util.TextRange

abstract class PostFormatProcessorTest : BasePlatformTestCase() {
    private val processor = PostFormatProcessor()

    protected fun sort(filename: String, input: String): String {
        val file = myFixture.configureByText(filename, input)
        WriteCommandAction.runWriteCommandAction(project) {
            processor.processText(file, TextRange(0, input.length), CodeStyle.getSettings(file))
        }
        return file.viewProvider.document!!.text
    }
}