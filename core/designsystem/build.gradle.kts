plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.compose.resources")
    id("droidkaigi.primitive.spotless")
}

dependencies {
    commonMainImplementation(libs.material3)
}
