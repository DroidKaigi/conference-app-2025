package io.github.droidkaigi.confsched

import android.content.Context
import io.github.droidkaigi.confsched.data.about.LicensesJsonReader

class AndroidLicensesJsonReader(
    private val applicationContext: Context,
) : LicensesJsonReader {
    override suspend fun readLicensesJson(): String {
        return applicationContext.resources.openRawResource(R.raw.aboutlibraries).readBytes().decodeToString()
    }
}
