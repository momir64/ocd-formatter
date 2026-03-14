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
            source.name.endsWith(".go") -> sortBlocks(original, { it == "import (" }, { it == ")" }, true)
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
        isEnd: (String) -> Boolean = { !isStart(it) && it.isNotEmpty() },
        consumeEnd: Boolean = false
    ): String {
        val out = mutableListOf<String>()
        val lines = text.lines()
        var i = 0

        fun collectLogicalLine(): List<String> {
            val raw = mutableListOf(lines[i++])
            var depth = raw[0].count { it == '(' } - raw[0].count { it == ')' }
            while (i < lines.size && depth > 0) {
                raw += lines[i]
                depth += lines[i].count { it == '(' } - lines[i].count { it == ')' }
                i++
            }
            return raw
        }

        fun flush(block: MutableList<List<String>>) {
            val comparator = compareByDescending<List<String>> { it.joinToString("").length }
                .thenBy { it.joinToString("").replace('*', '~') }
            out += block.sortedWith(comparator).flatten()
            block.clear()
        }

        while (i < lines.size) {
            if (!isStart(lines[i].trim())) {
                out += lines[i++]
                continue
            }

            val block = mutableListOf<List<String>>()
            if (consumeEnd && i < lines.size) out += lines[i++]

            while (i < lines.size && !isEnd(lines[i].trim())) {
                if (lines[i].isBlank()) {
                    flush(block)
                    out += lines[i++]
                } else {
                    block += collectLogicalLine()
                }
            }

            flush(block)
            if (consumeEnd && i < lines.size) out += lines[i++]
        }

        return out.joinToString("\n")
    }
}