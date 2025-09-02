package io.github.droidkaigi.confsched

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import io.github.droidkaigi.confsched.component.KaigiNavigationScaffold
import io.github.droidkaigi.confsched.component.MainScreenTab
import io.github.droidkaigi.confsched.model.about.AboutItem
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.core.defaultLang
import io.github.droidkaigi.confsched.navigation.component.NavHostWithSharedAxisX
import io.github.droidkaigi.confsched.navigation.extension.navigateToAboutTab
import io.github.droidkaigi.confsched.navigation.extension.navigateToContributors
import io.github.droidkaigi.confsched.navigation.extension.navigateToEventMapTab
import io.github.droidkaigi.confsched.navigation.extension.navigateToFavoritesTab
import io.github.droidkaigi.confsched.navigation.extension.navigateToLicenses
import io.github.droidkaigi.confsched.navigation.extension.navigateToProfileCardTab
import io.github.droidkaigi.confsched.navigation.extension.navigateToSearch
import io.github.droidkaigi.confsched.navigation.extension.navigateToSettings
import io.github.droidkaigi.confsched.navigation.extension.navigateToSponsors
import io.github.droidkaigi.confsched.navigation.extension.navigateToStaffs
import io.github.droidkaigi.confsched.navigation.extension.navigateToTimetableItemDetail
import io.github.droidkaigi.confsched.navigation.extension.navigateToTimetableTab
import io.github.droidkaigi.confsched.navigation.graph.aboutTabNavGraph
import io.github.droidkaigi.confsched.navigation.graph.eventMapTabNavGraph
import io.github.droidkaigi.confsched.navigation.graph.favoritesTabNavGraph
import io.github.droidkaigi.confsched.navigation.graph.profileTabNavGraph
import io.github.droidkaigi.confsched.navigation.graph.timetableTabNavGraph
import io.github.droidkaigi.confsched.navigation.route.AboutTabRoute
import io.github.droidkaigi.confsched.navigation.route.EventMapTabRoute
import io.github.droidkaigi.confsched.navigation.route.FavoritesTabRoute
import io.github.droidkaigi.confsched.navigation.route.ProfileTabRoute
import io.github.droidkaigi.confsched.navigation.route.TimetableTabRoute

@Composable
context(appGraph: AppGraph)
actual fun KaigiAppUi() {
    val navController = rememberNavController()
    val externalNavController = rememberExternalNavController()
    val hazeState = rememberHazeState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentTab by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.let { destination ->
                val mainTabRoutes = listOf(TimetableTabRoute, EventMapTabRoute, FavoritesTabRoute, AboutTabRoute, ProfileTabRoute)
                val matchedMainTabRoute = mainTabRoutes.firstOrNull { mainTabRoute -> destination.hasRoute(mainTabRoute.rootRouteClass) } ?: return@derivedStateOf null
                when (matchedMainTabRoute) {
                    TimetableTabRoute -> MainScreenTab.Timetable
                    EventMapTabRoute -> MainScreenTab.EventMap
                    FavoritesTabRoute -> MainScreenTab.Favorite
                    AboutTabRoute -> MainScreenTab.About
                    ProfileTabRoute -> MainScreenTab.Profile
                }
            }
        }
    }

    KaigiNavigationScaffold(
        currentTab = currentTab,
        hazeState = hazeState,
        onTabSelected = { tab ->
            when (tab) {
                MainScreenTab.Timetable -> navController.navigateToTimetableTab()
                MainScreenTab.EventMap -> navController.navigateToEventMapTab()
                MainScreenTab.Favorite -> navController.navigateToFavoritesTab()
                MainScreenTab.About -> navController.navigateToAboutTab()
                MainScreenTab.Profile -> navController.navigateToProfileCardTab()
            }
        },
    ) {
        NavHostWithSharedAxisX(
            navController = navController,
            startDestination = TimetableTabRoute,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState),
        ) {
            timetableTabNavGraph(
                onSearchClick = navController::navigateToSearch,
                onTimetableItemClick = navController::navigateToTimetableItemDetail,
                onBackClick = { navController.popBackStack() },
                onLinkClick = externalNavController::navigate,
                onShareClick = externalNavController::onShareClick,
                onAddCalendarClick = externalNavController::navigateToCalendarRegistration,
            )
            eventMapTabNavGraph(
                onClickReadMore = externalNavController::navigate,
            )
            favoritesTabNavGraph(
                onBackClick = { navController.popBackStack() },
                onLinkClick = externalNavController::navigate,
                onShareClick = externalNavController::onShareClick,
                onAddCalendarClick = externalNavController::navigateToCalendarRegistration,
                onTimetableItemClick = navController::navigateToTimetableItemDetail,
            )
            aboutTabNavGraph(
                onAboutItemClick = {
                    val portalBaseUrl = if (defaultLang() == Lang.JAPANESE) {
                        "https://portal.droidkaigi.jp"
                    } else {
                        "https://portal.droidkaigi.jp/en"
                    }
                    when (it) {
                        AboutItem.Map -> externalNavController.navigate(
                            url = "https://goo.gl/maps/vv9sE19JvRjYKtSP9",
                        )
                        AboutItem.Contributors -> navController.navigateToContributors()
                        AboutItem.Staff -> navController.navigateToStaffs()
                        AboutItem.Sponsors -> navController.navigateToSponsors()
                        AboutItem.CodeOfConduct -> {
                            externalNavController.navigate(
                                url = "$portalBaseUrl/about/code-of-conduct",
                            )
                        }
                        AboutItem.License -> navController.navigateToLicenses()
                        AboutItem.PrivacyPolicy -> {
                            externalNavController.navigate(
                                url = "$portalBaseUrl/about/privacy",
                            )
                        }
                        AboutItem.Settings -> navController.navigateToSettings()
                        AboutItem.Youtube -> {
                            externalNavController.navigate(
                                url = "https://www.youtube.com/c/DroidKaigi",
                            )
                        }
                        AboutItem.X -> {
                            externalNavController.navigate(
                                url = "https://x.com/DroidKaigi",
                            )
                        }
                        AboutItem.Medium -> {
                            externalNavController.navigate(
                                url = "https://medium.com/droidkaigi",
                            )
                        }
                    }
                },
                onBackClick = navController::popBackStack,
                onLinkClick = externalNavController::navigate,
            )
            profileTabNavGraph(
                onShareProfileCardClick = externalNavController::onShareProfileCardClick,
            )
        }
    }
}
