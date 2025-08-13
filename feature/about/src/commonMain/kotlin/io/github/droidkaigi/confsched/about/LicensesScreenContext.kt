package io.github.droidkaigi.confsched.about

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesGraphExtension
import io.github.droidkaigi.confsched.common.scope.LicensesScope
import io.github.droidkaigi.confsched.context.ScreenContext
import io.github.droidkaigi.confsched.model.about.LicensesQueryKey

@ContributesGraphExtension(LicensesScope::class)
interface LicensesScreenContext : ScreenContext {
    val licensesQueryKey: LicensesQueryKey

    @ContributesGraphExtension.Factory(AppScope::class)
    fun interface Factory {
        fun createLicensesScreenContext(): LicensesScreenContext
    }
}
