package io.github.droidkaigi.confsched

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.about.LicensesJsonReader

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    isExtendable = true,
)
interface JvmAppGraph : AppGraph {
    @DependencyGraph.Factory
    fun interface Factory {
        fun createJvmAppGraph(
            @Provides licensesJsonReader: LicensesJsonReader,
        ): JvmAppGraph
    }
}
