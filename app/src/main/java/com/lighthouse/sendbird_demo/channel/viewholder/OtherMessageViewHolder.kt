package com.lighthouse.sendbird_demo.channel.viewholder

import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lighthouse.sendbird_demo.databinding.OtherMessageBinding
import com.sendbird.android.channel.BaseChannel
import com.sendbird.android.message.BaseMessage
import com.sendbird.android.message.Reaction
import com.sendbird.android.message.SendingStatus
import com.sendbird.uikit.activities.viewholder.GroupChannelMessageViewHolder
import com.sendbird.uikit.consts.ClickableViewIdentifier
import com.sendbird.uikit.interfaces.OnItemClickListener
import com.sendbird.uikit.interfaces.OnItemLongClickListener
import com.sendbird.uikit.model.MessageListUIParams
import com.sendbird.uikit.utils.DrawableUtils
import java.util.concurrent.ConcurrentHashMap

class OtherMessageViewHolder(
    private val binding: OtherMessageBinding
) : GroupChannelMessageViewHolder(binding.root) {
    override fun getClickableViewMap(): MutableMap<String, View> {
        val viewMap = ConcurrentHashMap<String, View>()
        viewMap[ClickableViewIdentifier.Chat.name] = binding.tvMessage
        return viewMap
    }

    override fun setEmojiReaction(
        reactionList: MutableList<Reaction>,
        emojiReactionClickListener: OnItemClickListener<String>?,
        emojiReactionLongClickListener: OnItemLongClickListener<String>?,
        moreButtonClickListener: View.OnClickListener?
    ) {
//        TODO("Not yet implemented")
    }

    override fun bind(channel: BaseChannel, message: BaseMessage, params: MessageListUIParams) {
        val context = binding.root.context
        val sendState = message.sendingStatus == SendingStatus.SUCCEEDED
        binding.tvSentAt.visibility = if (sendState) View.VISIBLE else View.GONE
        val sentAt =
            DateUtils.formatDateTime(context, message.createdAt, DateUtils.FORMAT_SHOW_TIME)
        binding.tvSentAt.text = sentAt

        val sender = message.sender
        val nickname = if (sender == null || TextUtils.isEmpty(sender.nickname)) context.getString(
            com.sendbird.uikit.R.string.sb_text_channel_list_title_unknown
        ) else sender.nickname

        binding.tvNickname.text = nickname

        var url = ""
        if (sender != null && !TextUtils.isEmpty(sender.profileUrl)) {
            url = sender.profileUrl
        }

        val errorIcon = DrawableUtils.createOvalIcon(
            context,
            com.sendbird.uikit.R.color.background_300,
            com.sendbird.uikit.R.drawable.icon_user,
            com.sendbird.uikit.R.color.ondark_01
        )
        Glide.with(context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(errorIcon)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivProfileView)

        binding.tvMessage.text = message.message
    }
}