package io.github.droidkaigi.confsched.navigation.extension

import androidx.navigation.NavController
import io.github.droidkaigi.confsched.navigation.route.AboutTabRoute
import io.github.droidkaigi.confsched.navigation.route.ContributorsRoute
import io.github.droidkaigi.confsched.navigation.route.LicensesRoute
import io.github.droidkaigi.confsched.navigation.route.SettingsRoute
import io.github.droidkaigi.confsched.navigation.route.SponsorsRoute
import io.github.droidkaigi.confsched.navigation.route.StaffsRoute

fun NavController.navigateToAboutTab() {
    navigate(AboutTabRoute) {
        launchSingleTop = true
        popUpTo<AboutTabRoute>()
    }
}

fun NavController.navigateToSponsors() {
    navigate(SponsorsRoute)
}

fun NavController.navigateToLicenses() {
    navigate(LicensesRoute)
}

fun NavController.navigateToSettings() {
    navigate(SettingsRoute)
}

fun NavController.navigateToStaffs() {
    navigate(StaffsRoute)
}

fun NavController.navigateToContributors() {
    navigate(ContributorsRoute)
}
