package com.lighthouse.sendbird_demo

import android.os.Bundle
import com.sendbird.uikit.fragments.ChannelListFragment
import com.sendbird.uikit.modules.ChannelListModule

class CustomChannelList : ChannelListFragment() {
    override fun onCreateModule(args: Bundle): ChannelListModule {
        val module: ChannelListModule = super.onCreateModule(args)
        module.setHeaderComponent(CustomHeader())
        return module
    }
}