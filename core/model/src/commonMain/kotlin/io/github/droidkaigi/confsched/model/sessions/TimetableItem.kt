package io.github.droidkaigi.confsched.model.sessions

import io.github.droidkaigi.confsched.model.core.DroidKaigi2025Day
import io.github.droidkaigi.confsched.model.core.Lang
import io.github.droidkaigi.confsched.model.core.MultiLangText
import io.github.droidkaigi.confsched.model.core.Room
import io.github.droidkaigi.confsched.model.core.RoomType.RoomF
import io.github.droidkaigi.confsched.model.core.defaultLang
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.Instant

sealed class TimetableItem {
    abstract val id: TimetableItemId
    abstract val title: MultiLangText
    abstract val startsAt: Instant
    abstract val endsAt: Instant
    abstract val category: TimetableCategory
    abstract val sessionType: TimetableSessionType
    abstract val room: Room
    abstract val targetAudience: String
    abstract val language: TimetableLanguage
    abstract val asset: TimetableAsset
    abstract val levels: PersistentList<String>
    abstract val speakers: PersistentList<TimetableSpeaker>
    abstract val description: MultiLangText
    abstract val message: MultiLangText?

    val day: DroidKaigi2025Day? get() = DroidKaigi2025Day.ofOrNull(startsAt)

    @Serializable
    data class Session(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        @Contextual override val startsAt: Instant,
        @Contextual override val endsAt: Instant,
        override val category: TimetableCategory,
        override val sessionType: TimetableSessionType,
        override val room: Room,
        override val targetAudience: String,
        override val language: TimetableLanguage,
        override val asset: TimetableAsset,
        override val levels: PersistentList<String>,
        override val speakers: PersistentList<TimetableSpeaker>,
        override val description: MultiLangText,
        override val message: MultiLangText?,
    ) : TimetableItem() {
        companion object
    }

    @Serializable
    data class Special(
        override val id: TimetableItemId,
        override val title: MultiLangText,
        @Contextual override val startsAt: Instant,
        @Contextual override val endsAt: Instant,
        override val category: TimetableCategory,
        override val sessionType: TimetableSessionType,
        override val room: Room,
        override val targetAudience: String,
        override val language: TimetableLanguage,
        override val asset: TimetableAsset,
        override val levels: PersistentList<String>,
        override val speakers: PersistentList<TimetableSpeaker>,
        override val description: MultiLangText,
        override val message: MultiLangText?,
    ) : TimetableItem()

    private val startsDateString: String by lazy {
        val localDate = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        val year = localDate.year
        val month = localDate.month.number.toString().padStart(2, '0')
        val day = localDate.day.toString().padStart(2, '0')
        "$year.$month.$day"
    }

    val startsTimeString: String by lazy {
        startsAt.toTimetableTimeString()
    }

    val endsTimeString: String by lazy {
        endsAt.toTimetableTimeString()
    }

    val startsLocalTime: LocalTime by lazy {
        startsAt.toLocalTime()
    }

    val endsLocalTime: LocalTime by lazy {
        endsAt.toLocalTime()
    }

    val minutes: Int by lazy {
        (endsAt - startsAt).toInt(DurationUnit.MINUTES)
    }

    val formattedTimeString: String by lazy {
        "$startsTimeString ~ $endsTimeString"
    }

    val formattedDateTimeString: String by lazy {
        val prefix = MultiLangText(jaTitle = "分", enTitle = "min").currentLangTitle
        "$startsDateString / $formattedTimeString ($minutes$prefix)"
    }

    val formattedMonthAndDayString: String by lazy {
        val localDate = startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDate.month.number}".padStart(2, '0') + "/" + "${localDate.day}".padStart(2, '0')
    }

    val url: String
        get() = if (defaultLang() == Lang.JAPANESE) {
            "https://2024.droidkaigi.jp/timetable/${id.value}"
        } else {
            "https://2024.droidkaigi.jp/en/timetable/${id.value}"
        }

    fun getSupportedLangString(isJapaneseLocale: Boolean): String {
        val japanese = if (isJapaneseLocale) "日本語" else "Japanese"
        val english = if (isJapaneseLocale) "英語" else "English"
        val japaneseWithInterpretation =
            if (isJapaneseLocale) "日本語 (英語通訳あり)" else "Japanese (with English Interpretation)"
        val englishWithInterpretation =
            if (isJapaneseLocale) "英語 (日本語通訳あり)" else "English (with Japanese Interpretation)"

        return when (language.langOfSpeaker) {
            "JAPANESE" -> if (language.isInterpretationTarget) japaneseWithInterpretation else japanese
            "ENGLISH" -> if (language.isInterpretationTarget) englishWithInterpretation else english
            else -> language.langOfSpeaker
        }
    }
}

private fun Instant.toTimetableTimeString(): String {
    val localDate = toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDate.hour}".padStart(2, '0') + ":" + "${localDate.minute}".padStart(2, '0')
}

private fun Instant.toLocalTime(): LocalTime {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.time
}

fun TimetableItem.Session.Companion.fake(duration: Duration = 40.minutes): TimetableItem.Session {
    val startsAt = LocalDateTime.parse("2024-09-12T10:20:00").toInstant(TimeZone.of("UTC+9"))
    return TimetableItem.Session(
        id = TimetableItemId("2"),
        title = MultiLangText("DroidKaigiのアプリのアーキテクチャ", "DroidKaigi App Architecture"),
        startsAt = startsAt,
        endsAt = startsAt + duration,
        category = TimetableCategory(
            id = 28654,
            title = MultiLangText(
                "Android FrameworkとJetpack",
                "Android Framework and Jetpack",
            ),
        ),
        sessionType = TimetableSessionType.NORMAL,
        room = Room(
            id = 1,
            name = MultiLangText("Room1", "Room2"),
            type = RoomF,
            sort = 1,
        ),
        targetAudience = "For App developer アプリ開発者向け",
        language = TimetableLanguage(
            langOfSpeaker = "JAPANESE",
            isInterpretationTarget = true,
        ),
        asset = TimetableAsset(
            videoUrl = "https://www.youtube.com/watch?v=hFdKCyJ-Z9A",
            slideUrl = "https://droidkaigi.jp/2021/",
        ),
        speakers = listOf(
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
        ).toPersistentList(),
        description = MultiLangText(
            jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nhttps://github.com/DroidKaigi/conference-app-2024 これはURLです。\nこれはディスクリプションです。",
            enTitle = "This is a description.\nThis is a description.\nhttps://github.com/DroidKaigi/conference-app-2024 This is a URL.\nThis is a description.",
        ),
        message = MultiLangText(
            jaTitle = "このセッションは事情により中止となりました",
            enTitle = "This session has been cancelled due to circumstances.",
        ),
        levels = listOf(
            "INTERMEDIATE",
        ).toPersistentList(),
    )
}
