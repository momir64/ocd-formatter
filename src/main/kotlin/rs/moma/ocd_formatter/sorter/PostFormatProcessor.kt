package rs.moma.ocd_formatter.sorter

import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class PostFormatProcessor : PostFormatProcessor {
    override fun processElement(source: PsiElement, settings: CodeStyleSettings) = source

    override fun processText(source: PsiFile, range: TextRange, settings: CodeStyleSettings): TextRange {
        val document = source.viewProvider.document ?: return range
        val original = document.text
        val sorted = when {
            source.name.endsWith(".go") -> sortBlocks(original, isStart = { it == "import (" }, isEnd = { it == ")" })
            else -> importPredicate(source.name)?.let { sortBlocks(original, isStart = it) } ?: original
        }
        if (sorted != original) document.setText(sorted)
        return range
    }

    private fun importPredicate(name: String): ((String) -> Boolean)? {
        val ext = name.substringAfterLast('.', "")
        return when (ext) {
            "py" -> { s -> s.startsWith("import ") || s.startsWith("from ") }
            "java", "kt" -> { s -> s.startsWith("import ") }
            "cs" -> { s -> s.startsWith("using ") }
            "rs" -> { s -> s.startsWith("use ") }
            "js", "ts", "jsx", "tsx" -> { s ->
                s.startsWith("import ") || (s.contains("require(") &&
                        (s.startsWith("const ") || s.startsWith("let ") || s.startsWith("var ")))
            }

            else -> null
        }
    }

    private fun sortBlocks(
        text: String,
        isStart: (String) -> Boolean,
        isEnd: (String) -> Boolean = { !isStart(it) && it.isNotEmpty() }
    ): String {
        val out = mutableListOf<String>()
        val lines = text.lines()
        var i = 0

        fun flush(block: MutableList<String>) {
            val comparator = compareByDescending<String> { it.length }.thenBy { it.replace('*', '~') }
            out += block.sortedWith(comparator)
            block.clear()
        }

        while (i < lines.size) {
            if (!isStart(lines[i].trim())) {
                out += lines[i++]
                continue
            }

            val block = mutableListOf<String>()
            block += lines[i++]

            while (i < lines.size && !isEnd(lines[i].trim())) {
                if (lines[i].isBlank()) {
                    flush(block)
                    out += lines[i++]
                } else
                    block += lines[i++]
            }

            flush(block)
            if (i < lines.size)
                out += lines[i++]
        }

        return out.joinToString("\n")
    }
}
