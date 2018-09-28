package com.test.books.di.module

import android.app.Application
import com.test.books.data.local.AccountManager
import com.test.books.data.local.AccountManagerImpl
import com.test.books.data.local.AppSession
import com.test.books.data.local.SessionManager
import com.test.books.data.repository.LocalRepository
import com.test.books.data.repository.MockRepository
import com.test.books.data.repository.RestRepository
import com.test.books.data.rest.service.RestAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Application {
        return this.application
    }

    @Provides
    @Singleton
    fun provideRestRepository(restAdapter: RestAdapter): RestRepository {
        return RestRepository(restAdapter)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(): LocalRepository {
        return LocalRepository()
    }

    @Provides
    @Singleton
    fun provideMockRepository(): MockRepository {
        return MockRepository()
    }

    @Provides
    @Singleton
    fun provideRestAdapter(): RestAdapter {
        return RestAdapter()
    }

    @Provides
    @Singleton
    fun provideAccountManager(): AccountManager{
        return AccountManagerImpl()
    }

    @Provides
    @Singleton
    fun provideSessionManager(accountManager: AccountManager, restAdapter: RestAdapter): AppSession {
        return SessionManager(accountManager, restAdapter, application)
    }
}
