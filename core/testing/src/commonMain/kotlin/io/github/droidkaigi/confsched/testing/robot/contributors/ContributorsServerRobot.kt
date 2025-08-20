package io.github.droidkaigi.confsched.testing.robot.contributors

import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.contributors.FakeContributorsApiClient

interface ContributorsServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupContributorServer(serverStatus: ServerStatus)
}

@Inject
class DefaultContributorsServerRobot(
    private val fakeContributorsApiClient: FakeContributorsApiClient,
) : ContributorsServerRobot {
    override fun setupContributorServer(serverStatus: ContributorsServerRobot.ServerStatus) {
        fakeContributorsApiClient.setup(
            when (serverStatus) {
                ContributorsServerRobot.ServerStatus.Operational -> FakeContributorsApiClient.Status.Operational
                ContributorsServerRobot.ServerStatus.Error -> FakeContributorsApiClient.Status.Error
            },
        )
    }
}
