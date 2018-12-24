package io.awesdroid.asrdemokt.events

/**
 * Created by awesdroid.
 */

class EventPageHome(var id: EventId?, var data: Any?) {

    enum class EventId {
        START_LISTENING,
        STOP_LISTENING,
        UI_ON_VOICE_START,
        UI_ON_VOICE,
        SPEECH_RECOGNIZED_RESULT
    }

    override fun toString(): String {
        return "EventPageHome{" +
                "id=" + id +
                ", data=" + data +
                '}'.toString()
    }
}
