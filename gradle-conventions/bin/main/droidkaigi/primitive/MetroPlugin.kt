package droidkaigi.primitive


/**
 * Precompiled [metro.gradle.kts][droidkaigi.primitive.Metro_gradle] script plugin.
 *
 * @see droidkaigi.primitive.Metro_gradle
 */
public
class MetroPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("droidkaigi.primitive.Metro_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
