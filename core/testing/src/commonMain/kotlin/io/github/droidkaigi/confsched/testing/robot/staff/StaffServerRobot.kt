package io.github.droidkaigi.confsched.testing.robot.staff

import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.staff.FakeStaffApiClient

interface StaffServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupStaffServer(serverStatus: ServerStatus)
}

@Inject
class DefaultStaffServerRobot(
    private val fakeStaffApiClient: FakeStaffApiClient,
) : StaffServerRobot {
    override fun setupStaffServer(serverStatus: StaffServerRobot.ServerStatus) {
        fakeStaffApiClient.setup(
            when (serverStatus) {
                StaffServerRobot.ServerStatus.Operational -> FakeStaffApiClient.Status.Operational
                StaffServerRobot.ServerStatus.Error -> FakeStaffApiClient.Status.Error
            },
        )
    }
}
