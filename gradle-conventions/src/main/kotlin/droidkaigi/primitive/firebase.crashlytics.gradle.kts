package droidkaigi.primitive

import util.library
import util.libs

plugins {
    id("com.google.firebase.crashlytics")
}

dependencies {
    add("implementation", libs.library("firebaseCrashlytics"))
}
