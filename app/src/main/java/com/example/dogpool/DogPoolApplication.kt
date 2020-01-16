package com.example.dogpool

import android.app.Application
import timber.log.Timber

/**
 * Created by Mayokun Adeniyi on 06/01/2020.
 */
class DogPoolApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}