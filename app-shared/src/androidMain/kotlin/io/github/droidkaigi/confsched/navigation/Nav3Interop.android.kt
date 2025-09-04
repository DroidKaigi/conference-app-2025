package io.github.droidkaigi.confsched.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.SceneStrategy
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator

@Composable
actual fun rememberNavBackStack(vararg elements: NavKey): SnapshotStateList<NavKey> {
    return rememberNavBackStack(*elements)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
actual fun listDetailSceneStrategyListPaneMetaData(): Map<String, Any> {
    return ListDetailSceneStrategy.listPane()
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
actual fun listDetailSceneStrategyDetailPaneMetaData(): Map<String, Any> {
    return ListDetailSceneStrategy.detailPane()
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun sceneStrategy(): SceneStrategy<NavKey> {
    // To pop the continuous list pane's backstack one by one using PopLatest.
    // https://github.com/DroidKaigi/conference-app-2025/pull/539
    return rememberListDetailSceneStrategy(BackNavigationBehavior.PopLatest)
}

@Composable
actual fun platformEntryDecorators(): List<NavEntryDecorator<*>> {
    return listOf(
        rememberSceneSetupNavEntryDecorator(),
        rememberSavedStateNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator(), // This decorator is only available on Android for now
    )
}
