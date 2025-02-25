package com.pubscale.basicvideoplayer

import android.app.Application

class BasicVideoPlayerApp: Application() {

    companion object{
        lateinit var instance: BasicVideoPlayerApp
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}