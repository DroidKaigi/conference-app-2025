package io.github.droidkaigi.confsched.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.SceneStrategy
import androidx.navigation3.ui.SinglePaneSceneStrategy
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator

@Composable
actual fun rememberNavBackStack(vararg elements: NavKey): SnapshotStateList<NavKey> {
    return rememberSaveable { mutableStateListOf(*elements) }
}

actual fun listDetailSceneStrategyListPaneMetaData(): Map<String, Any> {
    return emptyMap()
}

actual fun listDetailSceneStrategyDetailPaneMetaData(): Map<String, Any> {
    return emptyMap()
}

@Composable
actual fun sceneStrategy(): SceneStrategy<NavKey> {
    return SinglePaneSceneStrategy()
}

@Composable
actual fun platformEntryDecorators(): List<NavEntryDecorator<*>> {
    return listOf(
        rememberSceneSetupNavEntryDecorator(),
        rememberSavedStateNavEntryDecorator(),
        // 2025/09/02 Temporary workaround for a ViewModel data retaining issue.
        // ViewModelStoreNavEntryDecorator is currently Android-only.
        // As a workaround, we copied its implementation and adapted it for the JVM target.
        rememberViewModelStoreNavEntryDecorator(),
    )
}
