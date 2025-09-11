package io.github.droidkaigi.confsched

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.JOptionPane
import kotlin.time.Instant

@Composable
actual fun rememberExternalNavController(): ExternalNavController {
    return remember { JvmExternalNavController() }
}

class JvmExternalNavController : ExternalNavController {
    override fun navigate(url: String) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            runCatching {
                Desktop.getDesktop().browse(URI(url))
            }.onFailure { error ->
                println("Failed to navigate to URL (invalid URL): $url error: $error")
            }
        } else {
            println("Failed to navigate to URL (desktop not supported): $url")
        }
    }

    override fun navigateToCalendarRegistration(timetableItem: TimetableItem) {
        fun Instant.toBasicISO8601String(): String {
            return this.toString()
                .replace("-", "")
                .replace(":", "")
                .replace(".", "")
        }

        fun String.encodeUtf8(): String {
            return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
        }

        val calendarUrl = buildString {
            append("https://calendar.google.com/calendar/r/eventedit")
            append("?text=").append("[${timetableItem.room.name.currentLangTitle}] ${timetableItem.title.currentLangTitle}".encodeUtf8())
            append("&dates=").append("${timetableItem.startsAt.toBasicISO8601String()}/${timetableItem.endsAt.toBasicISO8601String()}".encodeUtf8())
            append("&details=").append(timetableItem.url.encodeUtf8())
            append("&location=").append(timetableItem.room.name.currentLangTitle.encodeUtf8())
        }
        navigate(calendarUrl)
    }

    override fun onShareClick(timetableItem: TimetableItem) {
        val text = "[${timetableItem.room.name.currentLangTitle}] ${timetableItem.formattedMonthAndDayString} " +
            "${timetableItem.startsTimeString} - ${timetableItem.endsTimeString}\n" +
            "${timetableItem.title.currentLangTitle}\n" +
            "#DroidKaigi\n" +
            timetableItem.url
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, selection)

            JOptionPane.showMessageDialog(
                null,
                text,
                "Copied to clipboard!",
                JOptionPane.PLAIN_MESSAGE,
            )
        } catch (e: Exception) {
            println("Failed to copy to clipboard: ${e.message}")
        }
    }

    override fun onShareProfileCardClick(shareText: String, imageBitmap: ImageBitmap) {
        TODO("Not yet implemented")
    }
}
