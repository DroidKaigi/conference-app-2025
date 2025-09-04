package io.github.droidkaigi.confsched

import java.awt.Taskbar
import javax.imageio.ImageIO

/**
 * Sets the Dock/Taskbar icon only when the app is launched via Gradle `:run`.
 * No-ops for packaged distributions.
 */
fun applyRuntimeDockIconIfDev() {
    if (System.getProperty("app.devRun") != "true") return
    runCatching {
        if (!Taskbar.isTaskbarSupported()) return
        val url = ClassLoader.getSystemResource("ic_app_512.png") ?: return
        val image = ImageIO.read(url)
        Taskbar.getTaskbar().iconImage = image
    }
}
