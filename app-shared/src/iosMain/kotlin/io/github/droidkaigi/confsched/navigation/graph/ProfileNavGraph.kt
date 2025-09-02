package io.github.droidkaigi.confsched.navigation.graph

import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.confsched.profile.ProfileScreenRoot
import io.github.confsched.profile.rememberProfileScreenContextRetained
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.navigation.route.ProfileTabRoute

context(appGraph: AppGraph)
fun NavGraphBuilder.profileTabNavGraph(
    onShareProfileCardClick: (String, ImageBitmap) -> Unit,
) {
    composable<ProfileTabRoute> {
        with(rememberProfileScreenContextRetained()) {
            ProfileScreenRoot(
                onShareClick = onShareProfileCardClick,
            )
        }
    }
}
