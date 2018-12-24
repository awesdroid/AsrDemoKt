package io.awesdroid.asrdemokt.beans

/**
 * Created by awesdroid.
 */

class ChatItem {
    private var type: Int = 0
    private var content: String? = null

    fun getType(): Int {
        return type
    }

    fun setType(type: Int): ChatItem {
        this.type = type
        return this
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String): ChatItem {
        this.content = content
        return this
    }
}
