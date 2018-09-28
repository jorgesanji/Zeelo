package com.test.books.ui.application

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.squareup.picasso.Picasso
import com.test.books.di.component.ApplicationComponent
import com.test.books.di.component.DaggerApplicationComponent
import com.test.books.di.module.ApplicationModule

class SampleApplication : MultiDexApplication() {

    var applicationComponent: ApplicationComponent? = null
        set

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initGraph()
        initPicasso()
        instance = this
    }

    private fun initPicasso() {
        Picasso.with(this).isLoggingEnabled = true
    }

    private fun initGraph() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    companion object {

        var instance: SampleApplication? = null
            private set
    }
}
