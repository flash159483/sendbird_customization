package com.lighthouse.sendbird_demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    val count = MutableLiveData<Int>()

}