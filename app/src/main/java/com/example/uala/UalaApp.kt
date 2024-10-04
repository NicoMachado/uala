package com.example.uala

import android.app.Application
import com.example.uala.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UalaApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UalaApp)
            modules(
                modules = appModule
            )
        }
    }
}