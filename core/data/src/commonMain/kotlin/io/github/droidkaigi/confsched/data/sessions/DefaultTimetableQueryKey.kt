package io.github.droidkaigi.confsched.data.sessions

import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.github.droidkaigi.confsched.data.DataScope
import io.github.droidkaigi.confsched.data.core.toMultiLangText
import io.github.droidkaigi.confsched.data.core.toRoom
import io.github.droidkaigi.confsched.data.sessions.response.SessionAssetResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse
import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.data.TimetableQueryKey
import io.github.droidkaigi.confsched.model.sessions.Timetable
import io.github.droidkaigi.confsched.model.sessions.TimetableAsset
import io.github.droidkaigi.confsched.model.sessions.TimetableCategory
import io.github.droidkaigi.confsched.model.sessions.TimetableItem
import io.github.droidkaigi.confsched.model.sessions.TimetableItemId
import io.github.droidkaigi.confsched.model.sessions.TimetableLanguage
import io.github.droidkaigi.confsched.model.sessions.TimetableSessionType
import io.github.droidkaigi.confsched.model.sessions.TimetableSpeaker
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import soil.query.QueryId
import soil.query.QueryPreloadData
import soil.query.buildQueryKey
import kotlin.time.Instant

@ContributesBinding(DataScope::class, binding = binding<TimetableQueryKey>())
@Inject
public class DefaultTimetableQueryKey(
    private val sessionsApiClient: SessionsApiClient,
    private val dataStore: SessionCacheDataStore,
) : TimetableQueryKey by buildQueryKey(
    id = QueryId("timetable"),
    fetch = {
        val response = sessionsApiClient.sessionsAllResponse()
        dataStore.save(response)
        response.toTimetable()
    },
) {
    override fun onPreloadData(): QueryPreloadData<Timetable>? {
        return { dataStore.getCache()?.toTimetable() }
    }
}

public fun SessionsAllResponse.toTimetable(): Timetable {
    val timetableContents = this
    val speakerIdToSpeaker: Map<String, TimetableSpeaker> = timetableContents.speakers
        .groupBy { it.id }
        .mapValues { (_, apiSpeakers) ->
            apiSpeakers.map { apiSpeaker ->
                TimetableSpeaker(
                    id = apiSpeaker.id,
                    name = apiSpeaker.fullName,
                    bio = apiSpeaker.bio ?: "",
                    iconUrl = apiSpeaker.profilePicture.orEmpty(),
                    tagLine = apiSpeaker.tagLine ?: "",
                )
            }.first()
        }
    val categoryIdToCategory: Map<Int, TimetableCategory> = timetableContents.categories
        .flatMap { it.items }
        .groupBy { it.id }
        .mapValues { (_, apiCategories) ->
            apiCategories.map { apiCategory ->
                TimetableCategory(
                    id = apiCategory.id,
                    title = apiCategory.name.toMultiLangText(),
                )
            }.first()
        }
    val roomIdToRoom: Map<Int, Room> = timetableContents.rooms
        .associateBy(
            keySelector = { roomResponse -> roomResponse.id },
            valueTransform = { roomResponse -> roomResponse.toRoom() },
        )

    return Timetable(
        timetableItems = timetableContents.sessions.map { apiSession ->
            if (!apiSession.isServiceSession) {
                TimetableItem.Session(
                    id = TimetableItemId(apiSession.id),
                    title = apiSession.title.toMultiLangText(),
                    startsAt = apiSession.startsAt.toInstantAsJST(),
                    endsAt = apiSession.endsAt.toInstantAsJST(),
                    category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                    sessionType = TimetableSessionType.ofOrNull(apiSession.sessionType)!!,
                    room = roomIdToRoom[apiSession.roomId]!!,
                    targetAudience = apiSession.targetAudience,
                    language = TimetableLanguage(
                        langOfSpeaker = apiSession.language,
                        isInterpretationTarget = apiSession.interpretationTarget,
                    ),
                    asset = apiSession.asset.toTimetableAsset(),
                    description = if (apiSession.i18nDesc == null) {
                        MultiLangText(
                            jaTitle = apiSession.description ?: "",
                            enTitle = apiSession.description ?: "",
                        )
                    } else {
                        apiSession.i18nDesc.toMultiLangText()
                    },
                    speakers = apiSession.speakers
                        .map { speakerIdToSpeaker[it]!! }
                        .toPersistentList(),
                    message = apiSession.message?.toMultiLangText(),
                    levels = apiSession.levels.toPersistentList(),
                )
            } else {
                TimetableItem.Special(
                    id = TimetableItemId(apiSession.id),
                    title = apiSession.title.toMultiLangText(),
                    startsAt = apiSession.startsAt.toInstantAsJST(),
                    endsAt = apiSession.endsAt.toInstantAsJST(),
                    category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                    sessionType = TimetableSessionType.ofOrNull(apiSession.sessionType)!!,
                    room = roomIdToRoom[apiSession.roomId]!!,
                    targetAudience = apiSession.targetAudience,
                    language = TimetableLanguage(
                        langOfSpeaker = apiSession.language,
                        isInterpretationTarget = apiSession.interpretationTarget,
                    ),
                    asset = apiSession.asset.toTimetableAsset(),
                    speakers = apiSession.speakers
                        .map { speakerIdToSpeaker[it]!! }
                        .toPersistentList(),
                    levels = apiSession.levels.toPersistentList(),
                    description = if (apiSession.i18nDesc == null) {
                        MultiLangText(
                            jaTitle = apiSession.description ?: "",
                            enTitle = apiSession.description ?: "",
                        )
                    } else {
                        apiSession.i18nDesc.toMultiLangText()
                    },
                    message = apiSession.message?.toMultiLangText(),
                )
            }
        }
            .sortedWith(
                compareBy<TimetableItem> { it.startsAt }
                    .thenBy { it.room },
            )
            .toPersistentList(),
    )
}

private fun SessionAssetResponse.toTimetableAsset() = TimetableAsset(videoUrl, slideUrl)

internal fun String.toInstantAsJST(): Instant {
    val (date, _) = split("+")
    return LocalDateTime.parse(date).toInstant(TimeZone.of("UTC+9"))
}
