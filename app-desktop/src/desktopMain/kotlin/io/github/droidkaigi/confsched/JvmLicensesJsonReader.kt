package io.github.droidkaigi.confsched

import io.github.droidkaigi.confsched.data.about.LicensesJsonReader

class JvmLicensesJsonReader : LicensesJsonReader {
    override suspend fun readLicensesJson(): String {
        return object {}.javaClass.getResource("/licenses.json")?.readText().orEmpty()
    }
}
