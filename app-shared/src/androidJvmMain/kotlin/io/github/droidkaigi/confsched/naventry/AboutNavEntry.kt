package io.github.droidkaigi.confsched.naventry

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import io.github.droidkaigi.confsched.AppGraph
import io.github.droidkaigi.confsched.about.AboutScreenRoot
import io.github.droidkaigi.confsched.about.LicensesScreenRoot
import io.github.droidkaigi.confsched.about.rememberAboutScreenContextRetained
import io.github.droidkaigi.confsched.about.rememberLicensesScreenContextRetained
import io.github.droidkaigi.confsched.model.about.AboutItem
import io.github.droidkaigi.confsched.navigation.listDetailSceneStrategyDetailPaneMetaData
import io.github.droidkaigi.confsched.navigation.listDetailSceneStrategyListPaneMetaData
import io.github.droidkaigi.confsched.navkey.AboutNavKey
import io.github.droidkaigi.confsched.navkey.LicensesNavKey

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.aboutEntries(
    onAboutItemClick: (AboutItem) -> Unit,
    onBackClick: () -> Unit,
    aboutEntryMetadata: Map<String, Any> = emptyMap(),
) {
    aboutEntry(onAboutItemClick, aboutEntryMetadata)
    licensesEntry(onBackClick)
}

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.aboutEntry(
    onAboutItemClick: (AboutItem) -> Unit,
    metadata: Map<String, Any> = emptyMap(),
) {
    entry<AboutNavKey>(
        metadata = listDetailSceneStrategyListPaneMetaData() + metadata,
    ) {
        with(rememberAboutScreenContextRetained()) {
            AboutScreenRoot(
                onAboutItemClick = onAboutItemClick,
            )
        }
    }
}

context(appGraph: AppGraph)
fun EntryProviderBuilder<NavKey>.licensesEntry(
    onBackClick: () -> Unit,
) {
    entry<LicensesNavKey>(
        metadata = listDetailSceneStrategyDetailPaneMetaData(),
    ) {
        with(rememberLicensesScreenContextRetained()) {
            LicensesScreenRoot(
                onBackClick = onBackClick,
            )
        }
    }
}
