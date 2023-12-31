package com.lighthouse.sendbird_demo.channel_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lighthouse.sendbird_demo.R
import com.lighthouse.sendbird_demo.databinding.CustomChannelItemBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.user.Member
import com.sendbird.uikit.activities.adapter.ChannelListAdapter
import com.sendbird.uikit.activities.viewholder.BaseViewHolder

class CustomChannelListAdapter : ChannelListAdapter() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<GroupChannel> {
        val binding =
            CustomChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChannelListViewHolder(binding)
    }

    class ChannelListViewHolder(private val binding: CustomChannelItemBinding) :
        BaseViewHolder<GroupChannel>(binding.root) {
        override fun bind(item: GroupChannel) {
            val context = itemView.context
            val lastMessage = item.lastMessage

            //set title
            val members = item.members
            var opponent: Member
            if (members.size > 1) {
                val currentUser = SendbirdChat.currentUser?.nickname ?: " "
                opponent = if (members[0].nickname != currentUser) members[0] else members[1]
                binding.tvName.text = opponent.nickname
            } else {
                val channel = item.name
                binding.tvName.text = channel
            }

            // unread message count
            val unreadCount = item.unreadMessageCount
            binding.tvUnread.text =
                if (unreadCount > 99) context.getString(com.sendbird.uikit.R.string.sb_text_channel_list_unread_count_max) else unreadCount.toString()
            binding.tvUnread.visibility = if (unreadCount > 0) View.VISIBLE else View.GONE
            binding.tvUnread.setBackgroundResource(R.drawable.circle_textview)

            // channel cover image
            Glide.with(binding.ivProfileImg.context).load(item.coverUrl).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(binding.ivProfileImg)

            // last message whether user is typing or not
            if (item.isTyping && item.typingUsers.isNotEmpty()) {
                val typingUser = item.typingUsers
                if (typingUser.size == 1) {
                    binding.tvLastMessage.text = String.format(
                        context.getString(com.sendbird.uikit.R.string.sb_text_channel_typing_indicator_single),
                        typingUser[0].nickname
                    )
                }
            } else {
                if (lastMessage != null) {
                    binding.tvLastMessage.text = lastMessage.message
                }
            }

            //last message time
            item.lastMessage?.let {
                val time = it.createdAt
                val currentTime = System.currentTimeMillis()
                val timeDif = kotlin.math.abs(currentTime - time)

                val minutes = timeDif / (1000 * 60)
                val hours = minutes / 60
                val days = hours / 24

                // Determine the appropriate time format to display.
                val timeAgo = when {
                    days > 0 -> "$days days ago"
                    hours > 0 -> "$hours hours ago"
                    minutes > 0 -> "$minutes min ago"
                    else -> "Just now"
                }

                binding.tvLastMessageTime.text = timeAgo
            }

        }
    }
}