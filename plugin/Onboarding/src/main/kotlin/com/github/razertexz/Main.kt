package com.github.razertexz

import android.content.Context

import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.Utils
import com.aliucord.Http
import com.aliucord.utils.RxUtils.subscribe

import com.discord.stores.StoreStream

private data class Onboarding(
    val guild_id: Long,
    val prompts: List<Any>,
    val default_channel_ids: List<Long>,
    val enabled: Boolean,
    val mode: Int
)

@AliucordPlugin(requiresRestart = false)
class Main : Plugin() {
    override fun start(ctx: Context) {
        StoreStream.getGuildSelected().observeSelectedGuildId().subscribe { guildId ->
            Utils.threadPool.execute {
                val response = Http.Request.newDiscordRNRequest("https://discord.com/api/v9/guilds/$guildId/onboarding", "GET").execute()
                val onboarding = response.json(Onboarding::class.java)
                logger.warn(onboarding)
            }
        }
    }

    override fun stop(ctx: Context) = patcher.unpatchAll()
}
