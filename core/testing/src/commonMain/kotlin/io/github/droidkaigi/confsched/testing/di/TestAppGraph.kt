package io.github.droidkaigi.confsched.testing.di

import dev.zacsweers.metro.Binds
import io.github.droidkaigi.confsched.data.about.FakeBuildConfigProvider
import io.github.droidkaigi.confsched.data.about.FakeLicensesJsonReader
import io.github.droidkaigi.confsched.data.about.LicensesJsonReader
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.FakeContributorsApiClient
import io.github.droidkaigi.confsched.data.eventmap.EventMapApiClient
import io.github.droidkaigi.confsched.data.eventmap.FakeEventMapApiClient
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.data.staff.FakeStaffApiClient
import io.github.droidkaigi.confsched.data.staff.StaffApiClient
import io.github.droidkaigi.confsched.model.buildconfig.BuildConfigProvider

internal interface TestAppGraph :
    TimetableScreenTestGraph,
    AboutScreenTestGraph,
    ContributorsScreenTestGraph,
    EventMapScreenTestGraph,
    StaffScreenTestGraph {

    @Binds
    val FakeSessionsApiClient.binds: SessionsApiClient

    @Binds
    val FakeContributorsApiClient.binds: ContributorsApiClient

    @Binds
    val FakeStaffApiClient.binds: StaffApiClient

    @Binds
    val FakeBuildConfigProvider.binds: BuildConfigProvider

    @Binds
    val FakeLicensesJsonReader.binds: LicensesJsonReader

    @Binds
    val FakeEventMapApiClient.binds: EventMapApiClient
}

internal expect fun createTestAppGraph(): TestAppGraph
