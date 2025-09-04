/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Modifications (c) 2025 DroidKaigi committee
 * Reason: Required to ensure data stored on viewModel is retained in jvm target as expected
 * Changes:
 *   - Change the default value for shouldRemoveStoreOwner in rememberViewModelStoreNavEntryDecorator to a lambda that always returns true.
 *   - Remove SavedStateRegistryOwner and HasDefaultViewModelProviderFactory implementation from childViewModelStoreOwner in ViewModelStoreNavEntryDecorator to ensure it works in jvm target.
 *   - Replace NavEntry.key with NavEntry.contentKey, NavEntry.content.invoke(NavKey) with NavEntry.Content() to compile successfully.
 *   - Add suppress annotation to ViewModelStoreNavEntryDecorator to suppress ktlint warning.
 *   - Minor code formatting changes.
 */

package androidx.lifecycle.viewmodel.navigation3

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.navEntryDecorator

/**
 * Returns a [ViewModelStoreNavEntryDecorator] that is remembered across recompositions.
 *
 * @param [viewModelStoreOwner] The [ViewModelStoreOwner] that provides the [ViewModelStore] to
 *   NavEntries
 * @param [shouldRemoveStoreOwner] A lambda that returns a Boolean for whether the store owner for a
 *   [NavEntry] should be cleared when the [NavEntry] is popped from the backStack. If true, the
 *   entry's ViewModelStoreOwner will be removed.
 */
@Composable
public fun rememberViewModelStoreNavEntryDecorator(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    shouldRemoveStoreOwner: () -> Boolean = { true },
): NavEntryDecorator<Any> = remember {
    ViewModelStoreNavEntryDecorator(viewModelStoreOwner.viewModelStore, shouldRemoveStoreOwner)
}

/**
 * Provides the content of a [NavEntry] with a [ViewModelStoreOwner] and provides that
 * [ViewModelStoreOwner] as a [LocalViewModelStoreOwner] so that it is available within the content.
 *
 * This requires the usage of [androidx.navigation3.runtime.SavedStateNavEntryDecorator] to ensure
 * that the [NavEntry] scoped [ViewModel]s can properly provide access to
 * [androidx.lifecycle.SavedStateHandle]s
 *
 * @param [viewModelStore] The [ViewModelStore] that provides to NavEntries
 * @param [shouldRemoveStoreOwner] A lambda that returns a Boolean for whether the store owner for a
 *   [NavEntry] should be cleared when the [NavEntry] is popped from the backStack. If true, the
 *   entry's ViewModelStoreOwner will be removed.
 */
@Suppress("ktlint:standard:function-naming")
public fun ViewModelStoreNavEntryDecorator(
    viewModelStore: ViewModelStore,
    shouldRemoveStoreOwner: () -> Boolean,
): NavEntryDecorator<Any> {
    val storeOwnerProvider: EntryViewModel = viewModelStore.getEntryViewModel()
    val onPop: (Any) -> Unit = { key ->
        if (shouldRemoveStoreOwner()) {
            storeOwnerProvider.clearViewModelStoreOwnerForKey(key)
        }
    }
    return navEntryDecorator(onPop) { entry ->
        val viewModelStore = storeOwnerProvider.viewModelStoreForKey(entry.contentKey)

        val childViewModelStoreOwner = remember {
            object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore get() = viewModelStore
            }
        }

        CompositionLocalProvider(LocalViewModelStoreOwner provides childViewModelStoreOwner) {
            entry.Content()
        }
    }
}

private class EntryViewModel : ViewModel() {
    private val owners = mutableMapOf<Any, ViewModelStore>()

    fun viewModelStoreForKey(key: Any): ViewModelStore = owners.getOrPut(key) { ViewModelStore() }

    fun clearViewModelStoreOwnerForKey(key: Any) {
        owners.remove(key)?.clear()
    }

    override fun onCleared() {
        owners.forEach { (_, store) -> store.clear() }
    }
}

private fun ViewModelStore.getEntryViewModel(): EntryViewModel {
    val provider = ViewModelProvider.create(
        store = this,
        factory = viewModelFactory { initializer { EntryViewModel() } },
    )
    return provider[EntryViewModel::class]
}
