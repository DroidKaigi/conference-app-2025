package droidkaigi.primitive


/**
 * Precompiled [aboutlibraries.gradle.kts][droidkaigi.primitive.Aboutlibraries_gradle] script plugin.
 *
 * @see droidkaigi.primitive.Aboutlibraries_gradle
 */
public
class AboutlibrariesPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("droidkaigi.primitive.Aboutlibraries_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
