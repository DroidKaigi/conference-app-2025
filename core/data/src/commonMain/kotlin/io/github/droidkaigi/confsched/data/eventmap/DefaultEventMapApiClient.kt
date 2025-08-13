package io.github.droidkaigi.confsched.data.eventmap

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.core.NetworkExceptionHandler
import io.github.droidkaigi.confsched.data.core.toMultiLangText
import io.github.droidkaigi.confsched.data.core.toRoom
import io.github.droidkaigi.confsched.data.eventmap.response.EventMapResponse
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.eventmap.EventMapEvent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal interface EventMapApi {
    @GET("/events/droidkaigi2025/projects")
    suspend fun getEventMap(): EventMapResponse
}

@ContributesBinding(DataScope::class)
@Inject
public class DefaultEventMapApiClient(
    private val networkExceptionHandler: NetworkExceptionHandler,
    ktorfit: Ktorfit,
) : EventMapApiClient {
    private val eventMapApi = ktorfit.createEventMapApi()

    public override suspend fun eventMapEvents(): PersistentList<EventMapEvent> {
        return networkExceptionHandler {
            eventMapApi.getEventMap().toEventMapList()
        }
    }
}

public fun EventMapResponse.toEventMapList(): PersistentList<EventMapEvent> {
    val roomIdToRoom: Map<Int, Room> = this.rooms.associateBy(
        keySelector = { roomResponse -> roomResponse.id },
        valueTransform = { roomResponse -> roomResponse.toRoom() },
    )

    return this.projects
        .mapNotNull { project ->
            roomIdToRoom[project.roomId]?.let { room ->
                EventMapEvent(
                    name = project.title.toMultiLangText(),
                    room = room,
                    description = project.i18nDesc.toMultiLangText(),
                    moreDetailsUrl = project.moreDetailsUrl,
                    message = project.message?.toMultiLangText(),
                )
            }
        }
        .toPersistentList()
}
