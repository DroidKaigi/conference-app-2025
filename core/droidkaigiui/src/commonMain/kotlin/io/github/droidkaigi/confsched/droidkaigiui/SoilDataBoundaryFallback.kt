package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedMediumTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.component.AnimatedTextTopAppBar
import io.github.droidkaigi.confsched.droidkaigiui.component.DefaultSuspenseFallbackContent
import soil.plant.compose.reacty.ErrorBoundaryContext

sealed interface SoilDataBoundaryFallback {
    val suspenseFallback: @Composable () -> Unit
    val errorFallback: @Composable (ErrorBoundaryContext) -> Unit
}


private object NoAppBar : SoilDataBoundaryFallback {
    override val errorFallback: @Composable ((ErrorBoundaryContext) -> Unit)
        get() = TODO("Not yet implemented")
    override val suspenseFallback: @Composable (() -> Unit)
        get() = TODO("Not yet implemented")
}

private class AppBar(
    val title: String,
    val onBackClick: (() -> Unit)? = null,
    val size: AppBarSize = AppBarSize.Default,
) : SoilDataBoundaryFallback {
    @OptIn(ExperimentalMaterial3Api::class)
    private val appBarComposable: @Composable (title: String, onBackClick: (() -> Unit)?) -> Unit = { title, onBackClick ->
        when (size) {
            AppBarSize.Default -> {
                AnimatedTextTopAppBar(
                    title = title,
                    navigationIcon = {
                        onBackClick?.let {
                            IconButton(onClick = onBackClick) {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }

            AppBarSize.Medium -> {
                AnimatedMediumTopAppBar(
                    title = title,
                    onBackClick = { onBackClick?.invoke() },
                    navIconContentDescription = null,
                )
            }
        }
    }

    override val suspenseFallback: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                appBarComposable(title, onBackClick)
            }
        ) {
            DefaultSuspenseFallbackContent()
        }
    }

    override val errorFallback: @Composable (ErrorBoundaryContext) -> Unit = {
    }
}

enum class AppBarSize {
    Default,
    Medium,
}

private class Custom(
    override val suspenseFallback: @Composable () -> Unit,
    override val errorFallback: @Composable (ErrorBoundaryContext) -> Unit,
) : SoilDataBoundaryFallback

object SoilDataBoundaryDefaults {
    fun appBarFallback(
        title: String,
        onBackClick: (() -> Unit)? = null,
        appBarSize: AppBarSize = AppBarSize.Default,
    ): SoilDataBoundaryFallback = AppBar(
        title = title,
        onBackClick = onBackClick,
        size = appBarSize,
    )

    fun simpleFallback(): SoilDataBoundaryFallback = NoAppBar

    fun customFallback(
        suspenseFallback: @Composable () -> Unit,
        errorFallback: @Composable (ErrorBoundaryContext) -> Unit,
    ): SoilDataBoundaryFallback = Custom(
        suspenseFallback = suspenseFallback,
        errorFallback = errorFallback,
    )
}
