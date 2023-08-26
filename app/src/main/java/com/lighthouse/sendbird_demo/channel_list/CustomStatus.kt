package com.lighthouse.sendbird_demo.channel_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.lighthouse.sendbird_demo.R
import com.sendbird.uikit.modules.components.StatusComponent

class CustomStatus : StatusComponent() {
    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        args: Bundle?,
    ): View {
        params.errorText = "에러"
        params.emptyText = "채팅방 없음"
        params.emptyIcon = ContextCompat.getDrawable(context, R.drawable.multi)
        return super.onCreateView(context, inflater, parent, args)
    }
}