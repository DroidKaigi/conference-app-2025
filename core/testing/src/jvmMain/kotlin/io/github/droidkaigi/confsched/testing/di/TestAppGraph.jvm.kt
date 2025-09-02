package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.about.FakeBuildConfigProvider
import io.github.droidkaigi.confsched.data.about.FakeLicensesJsonReader
import io.github.droidkaigi.confsched.data.contributors.DefaultContributorsApiClient
import io.github.droidkaigi.confsched.data.eventmap.DefaultEventMapApiClient
import io.github.droidkaigi.confsched.data.sessions.DefaultSessionsApiClient
import io.github.droidkaigi.confsched.data.staff.DefaultStaffApiClient
import kotlinx.coroutines.CoroutineDispatcher

@DependencyGraph(
    scope = AppScope::class,
    additionalScopes = [DataScope::class],
    isExtendable = true,
    excludes = [
        DefaultSessionsApiClient::class,
        DefaultContributorsApiClient::class,
        DefaultEventMapApiClient::class,
        DefaultStaffApiClient::class,
        CoroutineDispatcher::class,
    ],
)
internal interface JvmTestAppGraph : TestAppGraph {
    @Provides
    fun provideFakeBuildConfigProvider(): FakeBuildConfigProvider = FakeBuildConfigProvider()

    @Provides
    fun provideFakeLicensesJsonReader(): FakeLicensesJsonReader = FakeLicensesJsonReader()
}

internal actual fun createTestAppGraph(): TestAppGraph {
    return createGraph<JvmTestAppGraph>()
}
