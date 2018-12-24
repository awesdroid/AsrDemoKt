package io.awesdroid.asrdemokt.events

/**
 * @auther Awesdroid
 */
class EventMainActivity(var id: EventId?) {
    enum class EventId {
        START_REC,
        STOP_REC
    }

    override fun toString(): String {
        return "EventMainActivity{" + id + '}'.toString()
    }
}
