package com.geckosoftlabs.mentorr

import android.app.Application

class MentorrApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MentorrApp
            private set
    }
}