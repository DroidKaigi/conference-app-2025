package droidkaigi.convention


/**
 * Precompiled [kmp-feature.gradle.kts][droidkaigi.convention.Kmp_feature_gradle] script plugin.
 *
 * @see droidkaigi.convention.Kmp_feature_gradle
 */
public
class KmpFeaturePlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("droidkaigi.convention.Kmp_feature_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
