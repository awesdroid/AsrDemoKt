package io.awesdroid.asrdemokt.ui

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.awesdroid.asrdemokt.R
import io.awesdroid.asrdemokt.beans.ChatItem
import io.awesdroid.asrdemokt.events.EventPageChat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * Created by awesdroid.
 */

class ChatPage : Fragment() {

    private var ctx: Context? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: ChatAdapter? = null
    private var chatList: MutableList<ChatItem>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.page_chat, container, false) as ViewGroup
        recyclerView = rootView.findViewById(R.id.rv_chat)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        chatList = ArrayList()
        adapter = ChatAdapter(context!!)
        adapter!!.setList(chatList!!)
        recyclerView!!.adapter = adapter
        recyclerView!!.scrollToPosition(adapter!!.itemCount - 1)
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onEventPageChat(event: EventPageChat) {
        Log.d(TAG, "onEventPageChat(): ChatPage $event")
        val id = event.id
        val data = event.data
        when (id) {
            EventPageChat.EventId.UPDATE_CHAT -> {
                val item = data as ChatItem
                updateChat(item)
            }
            else -> {
            }
        }
    }

    /**
     * Private methods
     */
    private fun updateChat(item: ChatItem) {
        chatList!!.add(item)
        // TODO: Here simulates a chatbot answer
        chatList!!.add(ChatItem().setType(TYPE_ROBOT).setContent("n/a"))

        recyclerView!!.adapter = adapter
        scrollChatListViewToBottom()
    }

    private fun scrollChatListViewToBottom() {
        recyclerView!!.post { recyclerView!!.scrollToPosition(adapter!!.itemCount - 1) }
    }

    companion object {
        private val TAG = ChatPage::class.java.simpleName

        val TYPE_PEOPLE = 0
        val TYPE_ROBOT = 1
        val TYPE_UNKNOWN = -1
    }

}
