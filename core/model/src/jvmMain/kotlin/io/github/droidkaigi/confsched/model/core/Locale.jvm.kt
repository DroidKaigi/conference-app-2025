package io.github.droidkaigi.confsched.model.core

actual fun getDefaultLocale(): Locale {
    return if (java.util.Locale.getDefault() == java.util.Locale.JAPAN) {
        Locale.JAPAN
    } else {
        Locale.OTHER
    }
}
