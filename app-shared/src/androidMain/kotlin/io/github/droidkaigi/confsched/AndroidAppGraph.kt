package io.github.droidkaigi.confsched

import android.content.Context
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
interface AndroidAppGraph : AppGraph {
    @DependencyGraph.Factory
    fun interface Factory {
        fun createAndroidAppGraph(
            @Provides applicationContext: Context,
            @Provides licensesJsonReader: LicensesJsonReader,
        ): AndroidAppGraph
    }
}
