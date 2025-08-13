package util

internal fun getDefaultPackageName(moduleName: String): String {
    return "io.github.droidkaigi.confsched.${moduleName.replace("-", "_")}"
}
