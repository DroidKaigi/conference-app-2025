package io.github.droidkaigi.confsched

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.droidkaigi.confsched.component.KaigiNavigationScaffold
import io.github.droidkaigi.confsched.component.MainScreenTab
import io.github.droidkaigi.confsched.component.NavDisplayWithSharedAxisX
import io.github.droidkaigi.confsched.model.about.AboutItem
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.core.defaultLang
import io.github.droidkaigi.confsched.naventry.aboutEntries
import io.github.droidkaigi.confsched.naventry.contributorsEntry
import io.github.droidkaigi.confsched.naventry.eventMapEntry
import io.github.droidkaigi.confsched.naventry.favoritesEntry
import io.github.droidkaigi.confsched.naventry.profileNavEntry
import io.github.droidkaigi.confsched.naventry.sessionEntries
import io.github.droidkaigi.confsched.naventry.staffEntry
import io.github.droidkaigi.confsched.navigation.rememberNavBackStack
import io.github.droidkaigi.confsched.navigation.sceneStrategy
import io.github.droidkaigi.confsched.navkey.AboutNavKey
import io.github.droidkaigi.confsched.navkey.ContributorsNavKey
import io.github.droidkaigi.confsched.navkey.EventMapNavKey
import io.github.droidkaigi.confsched.navkey.FavoritesNavKey
import io.github.droidkaigi.confsched.navkey.ProfileNavKey
import io.github.droidkaigi.confsched.navkey.LicensesNavKey
import io.github.droidkaigi.confsched.navkey.SearchNavKey
import io.github.droidkaigi.confsched.navkey.StaffNavKey
import io.github.droidkaigi.confsched.navkey.TimetableItemDetailNavKey
import io.github.droidkaigi.confsched.navkey.TimetableNavKey

context(appGraph: AppGraph)
@Composable
actual fun KaigiAppUi() {
    val backStack = rememberNavBackStack(TimetableNavKey)
    val externalNavController = rememberExternalNavController()
    val hazeState = rememberHazeState()

    KaigiNavigationScaffold(
        currentTab = backStack.lastOrNull()?.let {
            when (it) {
                is TimetableNavKey -> MainScreenTab.Timetable
                is EventMapNavKey -> MainScreenTab.EventMap
                is FavoritesNavKey -> MainScreenTab.Favorite
                is AboutNavKey -> MainScreenTab.About
                is ProfileNavKey -> MainScreenTab.Profile
                else -> null
            }
        },
        onTabSelected = {
            val navKey = when (it) {
                MainScreenTab.Timetable -> TimetableNavKey
                MainScreenTab.EventMap -> EventMapNavKey
                MainScreenTab.Favorite -> FavoritesNavKey
                MainScreenTab.About -> AboutNavKey
                MainScreenTab.Profile -> ProfileNavKey
            }
            backStack.clear()
            backStack.add(navKey)
        },
        hazeState = hazeState,
    ) {
        NavDisplayWithSharedAxisX(
            backStack = backStack,
            onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
            sceneStrategy = sceneStrategy(),
            entryProvider = entryProvider {
                sessionEntries(
                    onBackClick = { backStack.removeLastOrNull() },
                    onAddCalendarClick = externalNavController::navigateToCalendarRegistration,
                    onShareClick = externalNavController::onShareClick,
                    onLinkClick = externalNavController::navigate,
                    onSearchClick = { backStack.add(SearchNavKey) },
                    onTimetableItemClick = {
                        if (backStack.lastOrNull() is TimetableItemDetailNavKey) {
                            backStack.removeLastOrNull()
                        }
                        backStack.add(TimetableItemDetailNavKey(it))
                    }
                )
                contributorsEntry(
                    onBackClick = { backStack.removeLastOrNull() },
                    onContributorClick = externalNavController::navigate,
                )
                staffEntry(
                    onBackClick = { backStack.removeLastOrNull() },
                    onStaffItemClick = externalNavController::navigate,
                )
                favoritesEntry(
                    onTimetableItemClick = {
                        if (backStack.lastOrNull() is TimetableItemDetailNavKey) {
                            backStack.removeLastOrNull()
                        }
                        backStack.add(TimetableItemDetailNavKey(it))
                    }
                )
                eventMapEntry(
                    onClickReadMore = externalNavController::navigate,
                )
                aboutEntries(
                    onAboutItemClick = { item ->
                        val portalBaseUrl = if (defaultLang() == Lang.JAPANESE) {
                            "https://portal.droidkaigi.jp"
                        } else {
                            "https://portal.droidkaigi.jp/en"
                        }
                        when (item) {
                            AboutItem.Map -> {
                                externalNavController.navigate(
                                    url = "https://goo.gl/maps/vv9sE19JvRjYKtSP9",
                                )
                            }

                            AboutItem.Contributors -> backStack.add(ContributorsNavKey)
                            AboutItem.Staff -> backStack.add(StaffNavKey)
                            AboutItem.Sponsors -> TODO()
                            AboutItem.CodeOfConduct -> {
                                externalNavController.navigate(
                                    url = "$portalBaseUrl/about/code-of-conduct",
                                )
                            }

                            AboutItem.License -> backStack.add(LicensesNavKey)

                            AboutItem.PrivacyPolicy -> {
                                externalNavController.navigate(
                                    url = "$portalBaseUrl/about/privacy",
                                )
                            }

                            AboutItem.Settings -> TODO()
                            AboutItem.Youtube -> {
                                externalNavController.navigate(
                                    url = "https://www.youtube.com/c/DroidKaigi",
                                )
                            }

                            AboutItem.X -> {
                                externalNavController.navigate(
                                    url = "https://twitter.com/DroidKaigi",
                                )
                            }

                            AboutItem.Medium -> {
                                externalNavController.navigate(
                                    url = "https://medium.com/droidkaigi",
                                )
                            }
                        }
                    },
                    onBackClick = { backStack.removeLastOrNull() },
                )
                profileNavEntry()
            },
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
        )
    }
}
