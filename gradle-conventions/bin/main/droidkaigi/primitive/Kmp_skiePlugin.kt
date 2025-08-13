package droidkaigi.primitive


/**
 * Precompiled [kmp.skie.gradle.kts][droidkaigi.primitive.Kmp_skie_gradle] script plugin.
 *
 * @see droidkaigi.primitive.Kmp_skie_gradle
 */
public
class Kmp_skiePlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("droidkaigi.primitive.Kmp_skie_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
