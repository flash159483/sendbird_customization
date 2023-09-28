package com.lighthouse.sendbird_demo.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lighthouse.sendbird_demo.databinding.FragmentCountBinding
import com.lighthouse.sendbird_demo.viewmodel.ChatViewModel

class CountFragment : Fragment() {
    private lateinit var binding: FragmentCountBinding
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountBinding.inflate(inflater, container, false)
        initCount()
        return binding.root
    }

    private fun initCount() {
        binding.btnSend.setOnClickListener {
            val count = binding.etCount.text.toString()

            if (count.isNotEmpty()) {
                viewModel.count.value = count.toInt()
            }
        }
    }
}