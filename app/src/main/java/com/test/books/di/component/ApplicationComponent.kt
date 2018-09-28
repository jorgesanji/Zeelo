package com.test.books.di.component

import android.app.Application
import com.test.books.data.local.AccountManager
import com.test.books.data.local.AppSession
import com.test.books.data.repository.LocalRepository
import com.test.books.data.repository.MockRepository
import com.test.books.data.repository.RestRepository
import com.test.books.data.rest.service.RestAdapter
import com.test.books.di.module.ApplicationModule
import com.test.books.ui.application.SampleApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun provideApplication(): Application

    fun provideRestRepository(): RestRepository

    fun provideLocalRepository(): LocalRepository

    fun provideMockRepository(): MockRepository

    fun provideRestAdapter(): RestAdapter

    fun provideAccountManager(): AccountManager

    fun provideSessionManager(): AppSession

    fun inject(application: SampleApplication)
}
