package com.lighthouse.sendbird_demo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sendbird.uikit.modules.components.HeaderComponent

class CustomHeader : HeaderComponent() {
    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        args: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.custom_header, parent, false)
        return view
    }
}