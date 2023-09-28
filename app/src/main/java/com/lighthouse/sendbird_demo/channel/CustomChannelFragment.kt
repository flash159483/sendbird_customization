package com.lighthouse.sendbird_demo.channel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.lighthouse.sendbird_demo.channel.utils.StringSet
import com.lighthouse.sendbird_demo.viewmodel.ChatViewModel
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.params.UserMessageCreateParams
import com.sendbird.uikit.fragments.ChannelFragment
import com.sendbird.uikit.model.ReadyStatus
import com.sendbird.uikit.modules.ChannelModule
import com.sendbird.uikit.modules.components.MessageInputComponent
import com.sendbird.uikit.vm.ChannelViewModel

class CustomChannelFragment : ChannelFragment() {
    private val viewModels: ChatViewModel by activityViewModels()

    override fun onCreateModule(args: Bundle): ChannelModule {
        val module = super.onCreateModule(args)
        module.setHeaderComponent(CustomChannelHeader())
        module.setInputComponent(CustomChannelInputComponent())
        return module

    }

    override fun onBeforeReady(
        status: ReadyStatus,
        module: ChannelModule,
        viewModel: ChannelViewModel
    ) {
        super.onBeforeReady(status, module, viewModel)

        val channel = viewModel.channel ?: return

        module.messageListComponent.setAdapter(CustomListAdapter(channel))
    }

    override fun onBindMessageInputComponent(
        inputComponent: MessageInputComponent,
        viewModel: ChannelViewModel,
        channel: GroupChannel?
    ) {
        super.onBindMessageInputComponent(inputComponent, viewModel, channel)
        if (inputComponent is CustomChannelInputComponent) {
            val customInput = module.messageInputComponent as CustomChannelInputComponent
            customInput.voiceInput = View.OnClickListener {
                takeVoiceRecorder()
            }


            viewModels.count.observe(this) {
                val params = UserMessageCreateParams(it.toString()).apply {
                    customType = StringSet.count_type
                }

                channel?.sendUserMessage(params) { _, e ->
                    if (e != null) {
                        Toast.makeText(context, "Error: $e", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}