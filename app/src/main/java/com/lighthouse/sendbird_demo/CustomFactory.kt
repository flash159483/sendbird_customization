package com.lighthouse.sendbird_demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sendbird.uikit.fragments.UIKitFragmentFactory

class CustomFactory : UIKitFragmentFactory() {
    override fun newChannelListFragment(args: Bundle): Fragment {
        val fragment = CustomChannelList()
        fragment.arguments = args
        return fragment
    }
}