package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.Http
import com.aliucord.Utils
import com.aliucord.utils.RxUtils.subscribe

import com.discord.stores.StoreStream

private data class Onboarding(
    val guild_id: Long,
    val prompts: List<OnboardingPrompt>,
    val default_channel_ids: List<Long>,
    val enabled: Boolean,
    val mode: Int
)

private class OnboardingPrompt(
    val id: Long,
    val type: Int,
    val options: List<OnboardingPromptOption>,
    val title: String,
    val single_select: Boolean,
    val required: Boolean,
    val in_onboarding: Boolean
)

private class OnboardingPromptOption(
    val id: Long,
    val channel_ids: List<Long>,
    val role_ids: List<Long>,
    val emoji: Any?,
    val emoji_id: Long?,
    val emoji_name: String?
    val emoji_animated: Boolean?,
    val title: String,
    val description: String?
)

@AliucordPlugin(requiresRestart = false)
class Main : Plugin() {
    override fun start(ctx: Context) {
        StoreStream.getGuildSelected().observeSelectedGuildId().subscribe {
            val guildId = this

            Utils.threadPool.execute {
                val response = Http.Request.newDiscordRNRequest("https://discord.com/api/v9/guilds/$guildId/onboarding", "GET").execute()
                val onboarding = response.json(Onboarding::class.java)

                if (onboarding.enabled)
                    logger.warn(onboarding.toString())
            }
        }
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()
}
