package io.github.droidkaigi.confsched.navigation.extension

import androidx.navigation.NavController
import io.github.droidkaigi.confsched.navigation.route.AboutTabRoute
import io.github.droidkaigi.confsched.navigation.route.LicensesRoute

fun NavController.navigateToAboutTab() {
    navigate(AboutTabRoute) {
        launchSingleTop = true
        popUpTo<AboutTabRoute>()
    }
}

fun NavController.navigateToLicenses() {
    navigate(LicensesRoute)
}
