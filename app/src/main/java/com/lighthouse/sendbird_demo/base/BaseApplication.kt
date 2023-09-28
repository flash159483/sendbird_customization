package com.lighthouse.sendbird_demo.base

import android.app.Application
import android.util.Log
import com.lighthouse.sendbird_demo.BuildConfig
import com.lighthouse.sendbird_demo.channel.CustomChannelFragment
import com.lighthouse.sendbird_demo.channel_list.CustomChannelList
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.android.params.InitParams
import com.sendbird.uikit.SendbirdUIKit
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter
import com.sendbird.uikit.fragments.ChannelFragment
import com.sendbird.uikit.fragments.ChannelListFragment
import com.sendbird.uikit.interfaces.UserInfo
import com.sendbird.uikit.interfaces.providers.ChannelFragmentProvider
import com.sendbird.uikit.interfaces.providers.ChannelListFragmentProvider
import com.sendbird.uikit.model.configurations.UIKitConfig
import com.sendbird.uikit.providers.FragmentProviders

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initSendBirdUI()
        initSendBirdChat()
    }

    private fun initSendBirdChat() {
        SendbirdChat.init(InitParams(
            BuildConfig.SENDBIRD_APPLICATION, applicationContext, useCaching = true
        ), object : InitResultHandler {
            override fun onInitFailed(e: SendbirdException) {
                Log.i(
                    "SendBirdInit",
                    "Called when initialize failed. SDK will still operate properly as if useLocalCaching is set to false."
                )
            }

            override fun onInitSucceed() {
                SendbirdChat.connect(BuildConfig.SENDBIRD_USER_ID) { user, e ->
                    if (user != null) {
                        if (e != null) {
                            // Proceed in offline mode with the data stored in the local database.
                            // Later, connection is made automatically.
                            // and can be notified through ConnectionHandler.onReconnectSucceeded().
                        } else {
                        }
                    } else {
                        // Handle error.
                    }
                }
                Log.i("SendBirdInit", "Called when initialization is completed.")
            }

            override fun onMigrationStarted() {
                Log.i("SendBirdInit", "Called when there's an update in Sendbird server.")
            }
        })
    }

    private fun initSendBirdUI() {
        SendbirdUIKit.init(object : SendbirdUIKitAdapter {
            override fun getAppId(): String {
                return BuildConfig.SENDBIRD_APPLICATION
            }

            override fun getAccessToken(): String {
                return ""
            }

            override fun getUserInfo(): UserInfo {
                return object : UserInfo {
                    override fun getUserId(): String {
                        return BuildConfig.SENDBIRD_USER_ID
                    }

                    override fun getNickname(): String {
                        return BuildConfig.SENDBIRD_USER_NAME
                    }

                    override fun getProfileUrl(): String {
                        return ""
                    }
                }
            }

            override fun getInitResultHandler(): InitResultHandler {
                return object : InitResultHandler {
                    override fun onInitFailed(e: SendbirdException) {
                        // If DB migration fails, this method is called.
                    }

                    override fun onInitSucceed() {
                        // Init successful
                    }

                    override fun onMigrationStarted() {
                        // If DB migration is successful, this method is called and you can proceed to the next step.
                        // In the sample app, the `LiveData` class notifies you on the initialization progress
                        // And observes the `MutableLiveData<InitState> initState` value in `SplashActivity()`.
                        // If successful, the `LoginActivity` screen
                        // Or the `HomeActivity` screen will show.
                    }
                }
            }
        }, this)

        UIKitConfig.groupChannelConfig.enableTypingIndicator = true
        UIKitConfig.groupChannelConfig.enableReactions = false

        FragmentProviders.channelList = ChannelListFragmentProvider {
            ChannelListFragment.Builder().withArguments(it).setUseHeader(true)
                .setCustomFragment(CustomChannelList()).build()
        }

        FragmentProviders.channel = ChannelFragmentProvider { url, args ->
            ChannelFragment.Builder(url).withArguments(args).setUseHeader(true)
                .setCustomFragment(CustomChannelFragment()).build()
        }
    }
}