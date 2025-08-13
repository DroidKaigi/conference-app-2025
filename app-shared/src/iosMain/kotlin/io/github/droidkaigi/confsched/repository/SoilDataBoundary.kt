package io.github.droidkaigi.confsched.repository

import soil.query.core.DataModel
import soil.query.core.Reply
import soil.query.core.combine

fun <T> soilDataBoundary(
    state: DataModel<T>,
): T? {
    return when (val reply = state.reply) {
        is Reply.Some<T> -> reply.value
        is Reply.None -> null
    }
}

fun <T1, T2, RESULT> soilDataBoundary(
    state1: DataModel<T1>,
    state2: DataModel<T2>,
    transform: (T1, T2) -> RESULT
): RESULT? {
    val combinedReply = Reply.combine(
        state1.reply,
        state2.reply,
    ) { reply1, reply2 -> transform(reply1, reply2) }

    return when (combinedReply) {
        is Reply.None -> null
        is Reply.Some<RESULT> -> combinedReply.value
    }
}

fun <T1, T2, T3, RESULT> soilDataBoundary(
    state1: DataModel<T1>,
    state2: DataModel<T2>,
    state3: DataModel<T3>,
    transform: (T1, T2, T3) -> RESULT
): RESULT? {
    val combinedReply = Reply.combine(
        state1.reply,
        state2.reply,
        state3.reply,
    ) { reply1, reply2, reply3 -> transform(reply1, reply2, reply3) }

    return when (combinedReply) {
        is Reply.None -> null
        is Reply.Some<RESULT> -> combinedReply.value
    }
}
