package io.github.droidkaigi.confsched.data.staff

import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.model.staff.Staff
import io.github.droidkaigi.confsched.model.staff.fakes
import kotlinx.collections.immutable.PersistentList
import okio.IOException

@SingleIn(DataScope::class)
@Inject
public class FakeStaffApiClient : StaffApiClient {
    public sealed class Status : StaffApiClient {
        public data object Operational : Status() {
            override suspend fun staff(): PersistentList<Staff> {
                return Staff.fakes()
            }
        }

        public data object Error : Status() {
            override suspend fun staff(): PersistentList<Staff> {
                throw IOException("Fake IO Exception")
            }
        }
    }

    private var status: Status = Status.Operational

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun staff(): PersistentList<Staff> {
        return status.staff()
    }
}
