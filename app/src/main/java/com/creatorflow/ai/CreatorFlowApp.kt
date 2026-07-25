package com.creatorflow.ai

import android.app.Application
import com.creatorflow.ai.BuildConfig
import com.google.firebase.FirebaseApp
import com.razorpay.Checkout
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CreatorFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Checkout.preload(applicationContext)
    }
    companion object {
        lateinit var instance: CreatorFlowApp
            private set
    }
}