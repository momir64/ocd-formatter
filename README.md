# OCD Imports Formatter

A JetBrains IDE plugin that sorts import lines by length after you reformat code. Longest line first, shortest last, so the import block ends up as a tidy staircase.

```kotlin
import com.example.LongClassName
import com.example.Abc
import ab.cd.*
```

## How it works

The plugin registers a `PostFormatProcessor`, so it runs automatically after the IDE's normal *Reformat Code* action. Imports are grouped by blank lines, and each group is sorted on its own.

- Longest lines first, ties broken alphabetically.
- Wildcard imports (`*`) go after specific ones of the same length.
- Multi-line imports, such as Go's `import ( ... )` blocks, are sorted too, including the items inside them.
- Anything that isn't an import is left alone.

Supported: Java, Kotlin, Scala, Groovy, Swift, Python, C#, Rust, C/C++, Go, Ruby, PHP, Dart, Lua, JavaScript and TypeScript.

## Development

Built with Kotlin and the [IntelliJ Platform Gradle Plugin](https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html), targeting IntelliJ 2023.3+ and JDK 17.

```bash
./gradlew test           # per-language sorting tests
./gradlew runIde         # try it in a sandbox IDE
./gradlew buildPlugin    # package the plugin zip
```

The whole sorter is one file: [PostFormatProcessor.kt](src/main/kotlin/rs/moma/ocd_formatter/sorter/PostFormatProcessor.kt).

## License

[TWAORPWB](LICENSE) applies.
