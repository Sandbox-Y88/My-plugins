package com.github.razertexz

import android.content.Context
import android.view.View

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.Http

import com.discord.widgets.channels.list.items.ChannelListItemInvite
import com.discord.widgets.channels.list.`WidgetChannelsListAdapter$ItemInvite$onConfigure$1`

private data class Onboarding(
    val guild_id: Long,
    val prompts: List<Any>,
    val default_channel_ids: List<Long>,
    val enabled: Boolean,
    val mode: Any
)

@AliucordPlugin(requiresRestart = false)
class Main : Plugin() {
    override fun start(ctx: Context) {
        patcher.before<`WidgetChannelsListAdapter$ItemInvite$onConfigure$1`>("onClick", View::class.java) {
            Utils.threadPool.execute {
                val guildId = (`$data` as ChannelListItemInvite).guildId
                val response = Http.Request.newDiscordRNRequest("https://discord.com/api/v9/guilds/${guildId}/onboarding", "GET").execute()

                if (response.ok()) {
                    val onboarding = response.json(Onboarding::class.java)
                    logger.warn(onboarding)
                }
            }

            it.result = null
        }
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()
}
