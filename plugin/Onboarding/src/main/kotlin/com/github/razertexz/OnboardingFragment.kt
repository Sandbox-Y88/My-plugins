package com.github.razertexz

import android.view.View

import com.aliucord.Main

import com.discord.app.AppFragment

class OnboardingFragment() : AppFragment() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)
        Main.logger.info("${view.javaClass.name}"
    }
                         }
