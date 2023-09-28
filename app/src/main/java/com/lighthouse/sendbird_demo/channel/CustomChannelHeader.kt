package com.lighthouse.sendbird_demo.channel

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lighthouse.sendbird_demo.R
import com.lighthouse.sendbird_demo.databinding.CustomChannelHeaderBinding
import com.sendbird.android.SendbirdChat
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.user.Member
import com.sendbird.uikit.modules.components.ChannelHeaderComponent

class CustomChannelHeader : ChannelHeaderComponent() {
    private lateinit var binding: CustomChannelHeaderBinding

    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        args: Bundle?
    ): View {
        binding = CustomChannelHeaderBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.btnBack.setOnClickListener(this::onLeftButtonClicked)

        binding.tbProfile.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_info -> {
                    onRightButtonClicked(binding.root)
                    true
                }

                R.id.item_alarm -> {
                    Toast.makeText(context, "alarm clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    true
                }
            }
        }
        return binding.root
    }

    override fun notifyChannelChanged(channel: GroupChannel) {
        val members = channel.members
        val opponent: Member
        if (members.size > 1) {
            val currentUser = SendbirdChat.currentUser?.nickname ?: " "
            opponent =
                if (members[0].nickname != currentUser) members[0] else members[1]
            binding.tvOpponentName.text = opponent.nickname
            binding.tvOnline.text = opponent.connectionStatus.toString()
        } else {
            binding.tvOpponentName.text = channel.name
        }
    }

}