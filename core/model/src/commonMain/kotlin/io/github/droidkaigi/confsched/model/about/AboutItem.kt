package io.github.droidkaigi.confsched.model.about

import kotlinx.serialization.Serializable

@Serializable
enum class AboutItem {
    Map,

    Contributors,
    Staff,
    Sponsors,

    CodeOfConduct,
    License,
    PrivacyPolicy,
    Settings,

    Youtube,
    X,
    Medium,
}
