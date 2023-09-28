package com.lighthouse.sendbird_demo.channel

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lighthouse.sendbird_demo.channel.utils.StringSet
import com.lighthouse.sendbird_demo.channel.viewholder.MyCountViewHolder
import com.lighthouse.sendbird_demo.channel.viewholder.MyMessageViewHolder
import com.lighthouse.sendbird_demo.channel.viewholder.OtherCountViewHolder
import com.lighthouse.sendbird_demo.channel.viewholder.OtherMessageViewHolder
import com.lighthouse.sendbird_demo.databinding.MyCountBinding
import com.lighthouse.sendbird_demo.databinding.MyMessageBinding
import com.lighthouse.sendbird_demo.databinding.OtherCountBinding
import com.lighthouse.sendbird_demo.databinding.OtherMessageBinding
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.message.UserMessage
import com.sendbird.uikit.activities.adapter.MessageListAdapter
import com.sendbird.uikit.activities.viewholder.MessageType
import com.sendbird.uikit.activities.viewholder.MessageViewHolder
import com.sendbird.uikit.utils.MessageUtils

class CustomListAdapter(
    channel: GroupChannel,
) : MessageListAdapter(channel) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            COUNT_ME -> {
                val myCount = MyCountBinding.inflate(inflater, parent, false)
                val viewHolder = MyCountViewHolder(myCount)
                return applyClickListener(viewHolder)
            }

            COUNT_OTHER -> {
                val otherCount = OtherCountBinding.inflate(inflater, parent, false)
                val viewHolder = OtherCountViewHolder(otherCount)
                return applyClickListener(viewHolder)
            }

            MessageType.VIEW_TYPE_USER_MESSAGE_ME.value -> {
                val myMessage = MyMessageBinding.inflate(inflater, parent, false)
                val viewHolder = MyMessageViewHolder(myMessage)
                return applyClickListener(viewHolder)
            }

            MessageType.VIEW_TYPE_USER_MESSAGE_OTHER.value -> {
                val otherMessage = OtherMessageBinding.inflate(inflater, parent, false)
                val viewHolder = OtherMessageViewHolder(otherMessage)
                return applyClickListener(viewHolder)
            }


        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        val customType = message.customType

        if (!TextUtils.isEmpty(customType)
            && customType == StringSet.count_type
            && message is UserMessage
        ) {
            return when {
                MessageUtils.isMine(message) -> COUNT_ME
                else -> COUNT_OTHER
            }
        }
        return super.getItemViewType(position)
    }

    private fun applyClickListener(viewHolder: MessageViewHolder): MessageViewHolder {
        viewHolder.setMessageUIConfig(messageUIConfig)

        val views: Map<String, View> = viewHolder.clickableViewMap
        for (entry in views.entries) {
            val identifier = entry.key
            entry.value.setOnClickListener {
                val msgPosition = viewHolder.absoluteAdapterPosition
                if (msgPosition != -1) {
                    onListItemClickListener?.onIdentifiableItemClick(
                        it,
                        identifier,
                        msgPosition,
                        getItem(msgPosition)
                    )
                }
            }

            entry.value.setOnLongClickListener {
                val msgPosition = viewHolder.absoluteAdapterPosition
                if (msgPosition != -1) {
                    onListItemLongClickListener?.onIdentifiableItemLongClick(
                        it,
                        identifier,
                        msgPosition,
                        getItem(msgPosition)
                    )
                }
                true
            }
        }
        return viewHolder
    }

    companion object {
        private const val COUNT_ME = 10001
        private const val COUNT_OTHER = 10002
    }
}