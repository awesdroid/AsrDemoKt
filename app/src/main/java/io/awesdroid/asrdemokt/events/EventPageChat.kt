package io.awesdroid.asrdemokt.events

/**
 * Created by awesdroid.
 */

class EventPageChat(var id: EventId?, var data: Any?) {

    enum class EventId {
        UPDATE_CHAT
    }

    override fun toString(): String {
        return "EventPageChat{" +
                "id=" + id +
                ", data=" + data +
                '}'.toString()
    }
}
