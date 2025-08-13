package droidkaigi.primitive


/**
 * Precompiled [buildkonfig.gradle.kts][droidkaigi.primitive.Buildkonfig_gradle] script plugin.
 *
 * @see droidkaigi.primitive.Buildkonfig_gradle
 */
public
class BuildkonfigPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("droidkaigi.primitive.Buildkonfig_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
