package io.awesdroid.asrdemokt.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.awesdroid.asrdemokt.beans.ChatItem
import io.awesdroid.asrdemokt.ui.ChatPage.Companion.TYPE_PEOPLE
import io.awesdroid.asrdemokt.ui.ChatPage.Companion.TYPE_ROBOT
import io.awesdroid.asrdemokt.ui.ChatPage.Companion.TYPE_UNKNOWN
import io.awesdroid.asrdemokt.R


/**
 * @author awesdroid
 */
class ChatAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chatList: List<ChatItem>? = null

    fun setList(list: List<ChatItem>) {
        chatList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return if (viewType == TYPE_PEOPLE) {
            ViewHolderPeople(
                LayoutInflater.from(context)
                    .inflate(R.layout.lv_item_people, parent, false)
            )
        } else if (viewType == TYPE_ROBOT) {
            ViewHolderRobot(
                LayoutInflater.from(context)
                    .inflate(R.layout.lv_item_robot, parent, false)
            )
        } else {
            null
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (chatList != null && chatList!!.size != 0) {
            val item = chatList!![position]
            when (item.getType()) {
                TYPE_PEOPLE -> (holder as ViewHolderPeople).tvContent.text = item.getContent()
                TYPE_ROBOT -> (holder as ViewHolderRobot).tvContent.text = item.getContent()
                else -> {}
            }
        }

    }

    override fun getItemCount(): Int {
        return if (chatList != null && chatList!!.size != 0) {
            chatList!!.size
        } else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList != null && chatList!!.size != 0) {
            chatList!![position].getType()
        } else TYPE_UNKNOWN
    }

    internal inner class ViewHolderPeople(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvContent: TextView

        init {
            tvContent = itemView.findViewById(R.id.textView_people)
        }
    }


    internal inner class ViewHolderRobot(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvContent: TextView

        init {
            tvContent = itemView.findViewById(R.id.textView_robot)
        }
    }
}
