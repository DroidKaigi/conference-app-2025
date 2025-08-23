package io.github.confsched.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.confsched.profile.card.ProfileCardScreenRoot
import io.github.confsched.profile.edit.ProfileEditScreenRoot
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilDataBoundary
import io.github.droidkaigi.confsched.droidkaigiui.architecture.SoilFallbackDefaults
import io.github.droidkaigi.confsched.profile.ProfileRes
import io.github.droidkaigi.confsched.profile.profile_card_title
import org.jetbrains.compose.resources.stringResource
import soil.query.compose.rememberSubscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
context(screenContext: ProfileScreenContext)
fun ProfileScreenRoot() {
    SoilDataBoundary(
        state = rememberSubscription(screenContext.profileSubscriptionKey),
        fallback = SoilFallbackDefaults.appBar(stringResource(ProfileRes.string.profile_card_title)),
    ) { profile ->
        var isEditMode by remember { mutableStateOf(false) }
        when {
            profile != null && !isEditMode -> {
                ProfileCardScreenRoot(
                    profile = profile,
                    onEditClick = { isEditMode = true },
                )
            }

            else -> {
                ProfileEditScreenRoot(
                    profile = profile,
                    onProfileCreate = { isEditMode = false },
                )
            }
        }
    }
}
