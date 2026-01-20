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
            out += block.sortedWith(compareByDescending<String> { it.length }.thenBy { it })
            block.clear()
        }

        while (i < lines.size) {
            val t = lines[i].trim()
            if (!isStart(t)) {
                out += lines[i++]
                continue
            }

            val block = mutableListOf<String>()
            while (i < lines.size && !isEnd(lines[i].trim())) {
                if (lines[i].trim().isEmpty()) {
                    out += lines[i]
                    flush(block)
                } else
                    block += lines[i]
                i++
            }

            flush(block)
        }

        return out.joinToString("\n")
    }
}
