package droidkaigi.primitive


/**
 * Precompiled [detekt.gradle.kts][droidkaigi.primitive.Detekt_gradle] script plugin.
 *
 * @see droidkaigi.primitive.Detekt_gradle
 */
public
class DetektPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("droidkaigi.primitive.Detekt_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
