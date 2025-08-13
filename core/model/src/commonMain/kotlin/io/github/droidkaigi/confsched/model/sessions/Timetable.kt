package io.github.droidkaigi.confsched.model.sessions

import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Filters
import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.core.RoomType
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

data class Timetable(
    val timetableItems: PersistentList<TimetableItem> = persistentListOf(),
    val bookmarks: PersistentSet<TimetableItemId> = persistentSetOf(),
) {
    val contents: List<TimetableItemWithFavorite> by lazy {
        timetableItems.map {
            TimetableItemWithFavorite(it, bookmarks.contains(it.id))
        }
    }

    val rooms: List<Room> by lazy {
        timetableItems.map { it.room }.toSet().sorted()
    }

    val days: List<DroidKaigi2025Day> by lazy {
        timetableItems.mapNotNull { it.day }.toSet().sortedBy { it.ordinal }
    }

    val categories: List<TimetableCategory> by lazy {
        timetableItems.map { it.category }.toSet().sortedBy { it.id }
    }

    val sessionTypes: List<TimetableSessionType> by lazy {
        timetableItems.map { it.sessionType }.toSet().sorted()
    }

    val languages: List<TimetableLanguage> by lazy {
        timetableItems.map { it.language }.toSet()
            .sortedBy { it.langOfSpeaker }
            .sortedBy { it.isInterpretationTarget }
    }

    fun dayTimetable(droidKaigi2025Day: DroidKaigi2025Day): Timetable {
        var timetableItems = timetableItems.toList()
        timetableItems = timetableItems.filter { timetableItem ->
            timetableItem.day == droidKaigi2025Day
        }
        return copy(timetableItems = timetableItems.toPersistentList())
    }

    fun filtered(filters: Filters): Timetable {
        var timetableItems = timetableItems.toList()
        if (filters.days.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.days.contains(timetableItem.day)
            }
        }
        if (filters.categories.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.categories.contains(timetableItem.category)
            }
        }
        if (filters.sessionTypes.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.sessionTypes.contains(timetableItem.sessionType)
            }
        }
        if (filters.languages.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.languages.contains(timetableItem.language.toLang()) ||
                    timetableItem.language.isInterpretationTarget
            }
        }
        if (filters.filterFavorite) {
            timetableItems = timetableItems.filter { timetableItem ->
                bookmarks.contains(timetableItem.id)
            }
        }
        if (filters.searchWord.isNotBlank()) {
            timetableItems = timetableItems.filter { timetableItem ->
                timetableItem.title.currentLangTitle.contains(
                    filters.searchWord,
                    ignoreCase = true,
                )
            }
        }
        return copy(timetableItems = timetableItems.toPersistentList())
    }

    fun isEmpty(): Boolean {
        return timetableItems.isEmpty()
    }

    companion object
}

fun Timetable?.orEmptyContents(): Timetable = this ?: Timetable()

fun Timetable.Companion.fake(): Timetable {
    val rooms = mutableListOf(
        Room(1, MultiLangText("Meerkat", "Meerkat"), RoomType.RoomF, 4),
        Room(2, MultiLangText("Ladybug", "Ladybug"), RoomType.RoomG, 5),
        Room(3, MultiLangText("Koala", "Koala"), RoomType.RoomH, 1),
        Room(4, MultiLangText("Jellyfish", "Jellyfish"), RoomType.RoomI, 2),
        Room(5, MultiLangText("Narwhal", "Narwhal"), RoomType.RoomJ, 3),
    )
    repeat(10) {
        rooms += rooms
    }
    val roomsIterator = rooms.iterator()
    val timetableItems = buildList {
        add(
            TimetableItem.Special(
                id = TimetableItemId("1"),
                title = MultiLangText("ウェルカムトーク", "Welcome Talk"),
                startsAt = DroidKaigi2025Day.Workday.start + 10.hours,
                endsAt = DroidKaigi2025Day.Workday.start + 10.hours + 20.minutes,
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                sessionType = TimetableSessionType.NORMAL,
                room = roomsIterator.next(),
                targetAudience = "TBW",
                language = TimetableLanguage(
                    langOfSpeaker = "JAPANESE",
                    isInterpretationTarget = true,
                ),
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
                speakers = persistentListOf(
                    TimetableSpeaker(
                        id = "1",
                        name = "taka",
                        iconUrl = "https://github.com/takahirom.png",
                        bio = "Likes Android",
                        tagLine = "Android Engineer",
                    ),
                    TimetableSpeaker(
                        id = "2",
                        name = "ry",
                        iconUrl = "https://github.com/ry-itto.png",
                        bio = "Likes iOS",
                        tagLine = "iOS Engineer",
                    ),
                ),
                description = MultiLangText(
                    jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                        "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                    enTitle = "This is a description\nThis is a description\nThis is a description\n" +
                        "This is a description\nThis is a description\nThis is a description\n",
                ),
                message = null,
            ),
        )
        for (day in -1..1) {
            val dayOffsetSeconds = day * 24 * 60 * 60 + 10 * 60 * 60 + (0.5 * 60 * 60).toInt()
            for (index in 0..20) {
                val start = DroidKaigi2025Day.Workday.start + (index * 60 * 60 + dayOffsetSeconds).seconds
                val end = DroidKaigi2025Day.Workday.start + (index * 60 * 60 + dayOffsetSeconds + 40 * 60).seconds
                val fake = TimetableItem.Session.fake()
                add(
                    fake
                        .copy(
                            id = TimetableItemId("$day$index"),
                            title = MultiLangText(
                                jaTitle = "${fake.title.jaTitle} $day $index",
                                enTitle = "${fake.title.enTitle} $day $index",
                            ),
                            room = roomsIterator.next(),
                            startsAt = start,
                            endsAt = end,
                        ),
                )
            }
        }
        add(
            TimetableItem.Special(
                id = TimetableItemId("3"),
                title = MultiLangText("Closing", "Closing"),
                startsAt = DroidKaigi2025Day.Workday.start + 10.hours,
                endsAt = DroidKaigi2025Day.Workday.start + 10.hours + 20.minutes,
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                sessionType = TimetableSessionType.NORMAL,
                room = roomsIterator.next(),
                targetAudience = "TBW",
                language = TimetableLanguage(
                    langOfSpeaker = "ENGLISH",
                    isInterpretationTarget = true,
                ),
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
                speakers = persistentListOf(
                    TimetableSpeaker(
                        id = "1",
                        name = "taka",
                        iconUrl = "https://github.com/takahirom.png",
                        bio = "Likes Android",
                        tagLine = "Android Engineer",
                    ),
                    TimetableSpeaker(
                        id = "2",
                        name = "ry",
                        iconUrl = "https://github.com/ry-itto.png",
                        bio = "Likes iOS",
                        tagLine = "iOS Engineer",
                    ),
                ),
                description = MultiLangText(
                    jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                        "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                    enTitle = "This is a description\nThis is a description\nThis is a description\n" +
                        "This is a description\nThis is a description\nThis is a description\n",
                ),
                message = MultiLangText(
                    jaTitle = "このセッションは事情により中止となりました",
                    enTitle = "This session has been cancelled due to circumstances.",
                ),
            ),
        )
    }
    return Timetable(
        timetableItems = timetableItems.toPersistentList(),
        bookmarks = persistentSetOf(),
    )
}
