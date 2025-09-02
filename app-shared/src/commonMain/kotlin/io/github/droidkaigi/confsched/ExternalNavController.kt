package io.github.droidkaigi.confsched

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import io.github.droidkaigi.confsched.model.sessions.TimetableItem

@Composable
expect fun rememberExternalNavController(): ExternalNavController

interface ExternalNavController {
    fun navigate(url: String)
    fun navigateToCalendarRegistration(timetableItem: TimetableItem)
    fun onShareClick(timetableItem: TimetableItem)
    fun onShareProfileCardClick(shareText: String, imageBitmap: ImageBitmap)
}
