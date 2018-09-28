package com.test.books.di.module

import android.app.Activity

import com.test.books.di.PerFragment
import com.test.books.ui.view.AppNavigation
import com.test.books.ui.view.IONavigation

import dagger.Module
import dagger.Provides

@Module
class NavigatorModule(private val activity: Activity) {

    @Provides
    @PerFragment
    internal fun provideNavigator(): IONavigation {
        return AppNavigation(activity)
    }
}
