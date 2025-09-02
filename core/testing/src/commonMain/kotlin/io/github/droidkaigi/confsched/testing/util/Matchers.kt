package io.github.droidkaigi.confsched.testing.util

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import kotlin.test.assertFalse
import kotlin.test.assertTrue

fun hasTestTag(
    testTag: String,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
): SemanticsMatcher {
    return SemanticsMatcher(
        "TestTag ${if (substring) "contains" else "is"} '$testTag' (ignoreCase: $ignoreCase)",
    ) { node ->
        val nodeTestTag: String? = node.config.getOrNull(SemanticsProperties.TestTag)
        when {
            nodeTestTag == null -> false
            substring -> nodeTestTag.contains(testTag, ignoreCase)
            else -> nodeTestTag.equals(testTag, ignoreCase)
        }
    }
}

fun SemanticsNodeInteractionsProvider.onAllNodesWithTag(
    testTag: String,
    substring: Boolean = false,
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteractionCollection = onAllNodes(
    hasTestTag(testTag = testTag, substring = substring),
    useUnmergedTree = useUnmergedTree,
)

fun SemanticsNodeInteractionCollection.assertCountAtLeast(
    minimumExpectedSize: Int,
): SemanticsNodeInteractionCollection {
    val errorOnFail = "Failed to assert minimum count of nodes."
    val matchedNodes = fetchSemanticsNodes(
        atLeastOneRootRequired = minimumExpectedSize > 0,
        errorOnFail,
    )
    if (matchedNodes.size < minimumExpectedSize) {
        throw AssertionError(
            buildErrorMessageForMinimumCountMismatch(
                foundNodes = matchedNodes,
                minimumExpectedCount = minimumExpectedSize,
            ),
        )
    }
    return this
}

fun SemanticsNodeInteraction.assertTextDoesNotContain(
    substring: String,
) {
    fetchSemanticsNode()
        .let { node ->
            val textProperty = node
                .config
                .getOrNull(
                    key = SemanticsProperties.Text,
                )
            val textContent = textProperty
                ?.joinToString(
                    separator = " ",
                ) { it.text }
            assertFalse(
                message = "Node text contains unexpected substring: $substring",
                actual = textContent?.contains(substring) ?: false,
            )
        }
}

fun <T> SemanticsNodeInteraction.assertSemanticsProperty(
    key: SemanticsPropertyKey<T>,
    condition: (T?) -> Boolean,
) {
    fetchSemanticsNode()
        .let { node ->
            val actual = node
                .config
                .getOrNull(key)

            assertTrue(
                message = "Node has unexpected value for semantics property $key: $actual",
                actual = condition(actual),
            )
        }
}

private fun buildErrorMessageForMinimumCountMismatch(
    foundNodes: List<SemanticsNode>,
    minimumExpectedCount: Int,
): String {
    return buildString {
        appendLine("Failed to assert minimum count of nodes.")
        appendLine("Expected at least: $minimumExpectedCount")
        appendLine("Found: ${foundNodes.size}")
        appendLine("Matched nodes:")
        foundNodes.forEachIndexed { index, node ->
            appendLine("$index: $node")
        }
    }
}
