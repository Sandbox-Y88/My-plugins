package com.github.razertexz

import android.view.View

import com.aliucord.Utils

import com.discord.app.AppFragment

class OnboardingFragment() : AppFragment() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)

        Utils.showToast("${view.javaClass.name}")
    }
}
