package com.lighthouse.sendbird_demo.channel_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lighthouse.sendbird_demo.databinding.CustomHeaderBinding
import com.sendbird.uikit.modules.components.HeaderComponent

class CustomHeader : HeaderComponent() {
    var search: View.OnClickListener? = null
    var add: View.OnClickListener? = null

    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        args: Bundle?,
    ): View {
        val binding = CustomHeaderBinding.inflate(inflater, null, false)

        binding.btnSearch.setOnClickListener {
            search?.onClick(it)
        }

        binding.btnAdd.setOnClickListener {
            add?.onClick(it)
//             onRightButtonClicked(it)
        }
        return binding.root
    }
}