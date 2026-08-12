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
        return when (name.substringAfterLast('.', "")) {
            "py" -> { s -> s.startsWith("import ") || s.startsWith("from ") }
            "java", "kt", "swift", "scala", "groovy" -> { s -> s.startsWith("import ") }
            "cs" -> { s -> s.startsWith("using ") }
            "rs" -> { s -> s.startsWith("use ") }
            "cpp", "c", "h", "hpp" -> { s -> s.startsWith("#include ") }
            "rb" -> { s -> s.startsWith("require ") || s.startsWith("require_relative ") }
            "php" -> { s -> s.startsWith("use ") || s.startsWith("require ") || s.startsWith("include ") }
            "dart" -> { s -> s.startsWith("import ") || s.startsWith("part ") }
            "lua" -> { s -> s.startsWith("require(") }
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
        fun List<String>.removeEndComma() = dropLast(1) + last().trimEnd().removeSuffix(",")
        fun List<String>.join() = joinToString("", transform = String::trim)
        fun List<String>.addEndComma() = dropLast(1) + (last().trimEnd() + ",")
        fun List<String>.endsWithComma() = last().trimEnd().endsWith(",")
        fun String.depth() = count { it == '(' } - count { it == ')' }
        fun String.starSortKey(): String {
            val s = trim()
            val marker = if (s.trimEnd(',', ';').endsWith("*")) "1" else "0"
            return marker + s.replace('*', '~')
        }

        val out = mutableListOf<String>()
        val lines = text.lines()
        var i = 0

        fun collectLogicalLine(): List<String> {
            val raw = mutableListOf(lines[i++])
            var depth = raw[0].depth()
            while (i < lines.size && depth > 0) {
                raw += lines[i]
                depth += lines[i++].depth()
            }
            if (raw.size > 2) {
                var inner: List<String> = raw.subList(1, raw.size - 1).filter { it.isNotBlank() }
                val lastLineHasComma = inner.endsWithComma()
                if (!lastLineHasComma) inner = inner.addEndComma()
                val comparator = compareByDescending<String> { it.trim().length }.thenBy { it.starSortKey() }
                var sorted = inner.sortedWith(comparator)
                if (!lastLineHasComma) sorted = sorted.removeEndComma()
                return listOf(raw[0]) + sorted + listOf(raw.last())
            }
            return raw
        }

        fun flush(block: MutableList<List<String>>) {
            val comparator = compareByDescending<List<String>> { it.join().length }.thenBy { it.join().starSortKey() }
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