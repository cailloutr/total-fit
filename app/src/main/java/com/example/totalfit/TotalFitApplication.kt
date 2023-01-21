package com.example.totalfit

import android.app.Application
import com.example.totalfit.di.daoModule
import com.example.totalfit.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TotalFitApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TotalFitApplication)
            modules(
                listOf(
                    daoModule,
                    viewModelModule
                )
            )
        }
    }
}