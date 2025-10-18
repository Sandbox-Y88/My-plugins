package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.utils.RxUtils.subscribe

import com.discord.stores.StoreStream

private class Onboarding(
    @JvmField val guild_id: Long,
    @JvmField val prompts: List<OnboardingPrompt>,
    @JvmField val default_channel_ids: List<Long>,
    @JvmField val enabled: Boolean,
    @JvmField val mode: Int
) {
    class OnboardingPrompt(
        @JvmField val id: Long,
        @JvmField val type: Int,
        @JvmField val options: List<OnboardingPromptOption>,
        @JvmField val title: String,
        @JvmField val single_select: Boolean,
        @JvmField val required: Boolean,
        @JvmField val in_onboarding: Boolean
    ) {
        class OnboardingPromptOption(
            @JvmField val id: Long,
            @JvmField val channel_ids: List<Long>,
            @JvmField val role_ids: List<Long>,
            @JvmField val emoji: Any?,
            @JvmField val emoji_id: Long?,
            @JvmField val emoji_name: String?,
            @JvmField val emoji_animated: Boolean?,
            @JvmField val title: String,
            @JvmField val description: String?
        )
    }
}

@AliucordPlugin(requiresRestart = true)
class Main : Plugin() {
    override fun start(ctx: Context) {
        StoreStream.getGuildSelected().observeSelectedGuildId().subscribe {
            Utils.threadPool.execute {
                val response = Http.Request.newDiscordRNRequest("https://discord.com/api/v9/guilds/$this/onboarding", "GET").execute()

                try {
                    val onboarding = response.json(Onboarding::class.java)
                } finally {
                    response.close()
                }
            }
        }
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()
}