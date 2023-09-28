package com.lighthouse.sendbird_demo.channel

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.lighthouse.sendbird_demo.R
import com.lighthouse.sendbird_demo.databinding.CustomChannelInputBinding
import com.sendbird.android.channel.GroupChannel
import com.sendbird.android.message.BaseMessage
import com.sendbird.uikit.modules.components.MessageInputComponent
import com.sendbird.uikit.widgets.MessageInputView

class CustomChannelInputComponent : MessageInputComponent() {
    private lateinit var binding: CustomChannelInputBinding
    private var mode = MessageInputView.Mode.DEFAULT
    var voiceInput: View.OnClickListener? = null

    override fun onCreateView(
        context: Context,
        inflater: LayoutInflater,
        parent: ViewGroup,
        args: Bundle?
    ): View {
        binding = CustomChannelInputBinding.inflate(inflater, parent, false)

        binding.ivSend.setOnClickListener {
            onInputRightButtonClicked(it)
        }

        binding.ivCamera.setOnClickListener {
            onInputLeftButtonClicked(it)
        }

        binding.ivMick.setOnClickListener {
            voiceInput?.onClick(it)
        }

        binding.ivCross.setOnClickListener {
            requestInputMode(MessageInputView.Mode.DEFAULT)
        }

        binding.ivCount.setOnClickListener {
            binding.countPanel.apply {
                if (visibility == View.VISIBLE) {
                    visibility = View.GONE
                    binding.ivCount.setColorFilter(
                        ContextCompat.getColor(
                            getContext(), R.color.grey
                        )
                    )
                } else {
                    visibility = View.VISIBLE
                    binding.ivCount.setColorFilter(
                        ContextCompat.getColor(
                            getContext(), R.color.black
                        )
                    )
                }
            }
        }

        binding.etMessageInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onInputTextChanged(s ?: " ", start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                binding.etMessageInput.apply {
                    val lineCount = lineCount + 2
                    val maxLines = 5
                    val layoutParams = layoutParams
                    layoutParams.height = lineHeight * lineCount.coerceAtMost(maxLines) - 5
                    this.layoutParams = layoutParams

                }

                binding.ivSend.visibility = if (s?.isNotEmpty() != true) View.GONE else View.VISIBLE
            }
        })

        return binding.root
    }

    override fun getEditTextView(): EditText {
        return binding.etMessageInput
    }

    override fun getRootView(): View {
        return binding.root
    }

    override fun requestInputMode(mode: MessageInputView.Mode) {
        val before = this.mode
        this.mode = mode
        if (mode == MessageInputView.Mode.QUOTE_REPLY) {
            binding.ivSend.setOnClickListener(this::onInputRightButtonClicked)
        } else if (mode == MessageInputView.Mode.EDIT) {
            binding.ivSend.setOnClickListener(this::onEditModeSaveButtonClicked)
            binding.replyPanel.visibility = View.GONE
            binding.ivCross.visibility = View.GONE
        } else {
            binding.ivSend.setOnClickListener(this::onInputRightButtonClicked)
            binding.replyPanel.visibility = View.GONE
            binding.replyPanel.visibility = View.GONE
        }
        onInputModeChanged(before, mode)
    }

    override fun notifyDataChanged(
        message: BaseMessage?,
        channel: GroupChannel,
        defaultText: String
    ) {
        super.notifyDataChanged(message, channel, defaultText)

        if (mode == MessageInputView.Mode.QUOTE_REPLY) {
            if (message != null) {
                binding.replyPanel.text = applyColor(message)
                binding.replyPanel.visibility = View.VISIBLE
                binding.ivCross.visibility = View.VISIBLE
            }
        } else if (mode == MessageInputView.Mode.EDIT) {
            if (message != null) {
                binding.etMessageInput.setText(message.message)

            }
        } else {
            binding.etMessageInput.setText(defaultText)
        }
    }

    private fun applyColor(message: BaseMessage): SpannableStringBuilder {
        val m = SpannableStringBuilder()

        val first = SpannableString("reply to ${message.sender?.nickname ?: ""}\n")
        first.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            first.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        m.append(first)

        val second = SpannableString(message.message)
        second.setSpan(
            ForegroundColorSpan(Color.parseColor("#939393")),
            0,
            second.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        m.append(second)
        return m
    }
}