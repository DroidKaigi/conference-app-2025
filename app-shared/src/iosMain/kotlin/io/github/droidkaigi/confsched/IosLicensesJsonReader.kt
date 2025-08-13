package io.github.droidkaigi.confsched

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.app_shared.AppSharedRes
import io.github.droidkaigi.confsched.data.about.LicensesJsonReader

@ContributesBinding(AppScope::class)
@Inject
class IosLicensesJsonReader : LicensesJsonReader {
    override suspend fun readLicensesJson(): String {
        return AppSharedRes.readBytes("files/aboutlibraries.json").decodeToString()
    }
}
