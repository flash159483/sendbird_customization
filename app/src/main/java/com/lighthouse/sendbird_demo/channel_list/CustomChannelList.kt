package com.lighthouse.sendbird_demo.channel_list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sendbird.android.channel.GroupChannel
import com.sendbird.uikit.activities.ChannelActivity
import com.sendbird.uikit.activities.CreateChannelActivity
import com.sendbird.uikit.activities.MessageSearchActivity
import com.sendbird.uikit.fragments.ChannelListFragment
import com.sendbird.uikit.model.ReadyStatus
import com.sendbird.uikit.modules.ChannelListModule
import com.sendbird.uikit.modules.components.HeaderComponent
import com.sendbird.uikit.vm.ChannelListViewModel

class CustomChannelList : ChannelListFragment() {
    override fun onCreateModule(args: Bundle): ChannelListModule {
        val module: ChannelListModule = super.onCreateModule(args)
        module.setHeaderComponent(CustomHeader())
        module.setStatusComponent(CustomStatus())
        return module
    }

    override fun onBeforeReady(
        status: ReadyStatus,
        module: ChannelListModule,
        viewModel: ChannelListViewModel,
    ) {
        super.onBeforeReady(status, module, viewModel)
        module.channelListComponent.setAdapter(CustomChannelListAdapter())
        module.channelListComponent.setOnItemClickListener { _, _, channel ->
            GroupChannel.getChannel(channel.url) { _, e ->
                if (e != null) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    val intent = ChannelActivity.newIntent(
                        requireContext(),
                        channel.url
                    )
                    startActivity(intent)
                }
            }

        }
    }

    override fun onBindHeaderComponent(
        headerComponent: HeaderComponent,
        viewModel: ChannelListViewModel,
    ) {
        super.onBindHeaderComponent(headerComponent, viewModel)

        if (headerComponent is CustomHeader) {
            val header = module.headerComponent as CustomHeader
            header.search = View.OnClickListener {
                val intent = Intent(requireContext(), MessageSearchActivity::class.java)
                startActivity(intent)
            }

            header.add = View.OnClickListener {
                val intent = Intent(requireContext(), CreateChannelActivity::class.java)
                startActivity(intent)
            }
        }
    }
}