package com.safeword

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SafeWordApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.FEATURE_ADS_ENABLED) {
            MobileAds.initialize(this)
        }
    }
}

