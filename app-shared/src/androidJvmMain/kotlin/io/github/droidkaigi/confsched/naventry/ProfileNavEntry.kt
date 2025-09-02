package io.github.droidkaigi.confsched.naventry

import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import io.github.confsched.profile.ProfileScreenRoot
import io.github.confsched.profile.rememberProfileScreenContextRetained
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.navkey.ProfileNavKey

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.profileNavEntry(
    onShareProfileCardClick: (String, ImageBitmap) -> Unit,
) {
    entry<ProfileNavKey> {
        with(rememberProfileScreenContextRetained()) {
            ProfileScreenRoot(
                onShareClick = onShareProfileCardClick,
            )
        }
    }
}
