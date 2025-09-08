package io.github.droidkaigi.confsched.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink

private const val ClickableTextExpandAnimateDurationMillis = 300
private val LightBlue = Color(0xFF44ADE7)

/**
 * Provides ClickableText with underline for the specified regex.
 *
 * @param regex Specify a Regex to extract the string for which you want underlined text decoration
 */
@Composable
fun ClickableLinkText(
    style: TextStyle,
    content: String,
    onLinkClick: (url: String) -> Unit,
    regex: Regex,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    hasUnderLine: Boolean = true,
    underLineAndLinkTextColor: Color = LightBlue,
    onOverflow: (Boolean) -> Unit = {},
) {
    val findResults = findResults(
        content = content,
        regex = regex,
    )

    var isOverflowing by remember { mutableStateOf(false) }

    LaunchedEffect(isOverflowing) {
        onOverflow(isOverflowing)
    }

    Text(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(ClickableTextExpandAnimateDurationMillis, easing = EaseInQuart),
            ),
        text = buildClickableAnnotatedString(
            content = content,
            hasUnderLine = hasUnderLine,
            underLineColor = underLineAndLinkTextColor,
            findUrlResults = findResults,
            onLinkClick = onLinkClick,
        ),
        style = style,
        overflow = overflow,
        maxLines = maxLines,
        onTextLayout = { textLayoutResult ->
            isOverflowing = textLayoutResult.hasVisualOverflow
        },
    )
}

@Composable
private fun findResults(
    content: String,
    regex: Regex,
): Sequence<MatchResult> {
    return remember(content) {
        regex.findAll(content)
    }
}

@Composable
private fun buildClickableAnnotatedString(
    content: String,
    hasUnderLine: Boolean = true,
    underLineColor: Color,
    findUrlResults: Sequence<MatchResult>,
    onLinkClick: (url: String) -> Unit,
): AnnotatedString {
    return buildAnnotatedString {
        pushStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )

        var currentOffset = 0

        findUrlResults.forEach { matchResult ->
            val urlString = matchResult.value
            val matchStart = matchResult.range.first
            val matchEnd = matchResult.range.last + 1

            // Add text that is not a link
            if (matchStart > currentOffset) {
                append(content.substring(currentOffset, matchStart))
            }

            withLink(
                link = LinkAnnotation.Url(
                    url = urlString,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = underLineColor,
                            textDecoration = if (hasUnderLine) {
                                TextDecoration.Underline
                            } else {
                                null
                            },
                        ),
                    ),
                    linkInteractionListener = { linkAnnotation ->
                        if (linkAnnotation is LinkAnnotation.Url) {
                            onLinkClick(linkAnnotation.url)
                        }
                    },
                ),
                block = {
                    append(urlString)
                },
            )

            currentOffset = matchEnd
        }

        // Add the remaining text after the last URL
        if (currentOffset < content.length) {
            append(content.substring(currentOffset, content.length))
        }

        pop()
    }
}
