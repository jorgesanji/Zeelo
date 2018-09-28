package com.test.books.di.module

import com.test.books.business.usecase.books.CreateBookUseCase
import com.test.books.business.usecase.books.GetBookDetailUseCase
import com.test.books.business.usecase.books.GetBooksUseCase
import com.test.books.data.local.AppSession
import com.test.books.di.PerFragment
import com.test.books.ui.presenter.create.CreateBookPresenter
import com.test.books.ui.presenter.detail.DetailPresenter
import com.test.books.ui.presenter.home.HomePresenter
import com.test.books.ui.presenter.splash.SplashPresenter
import com.test.books.ui.utils.AppImagePickerManager
import com.test.books.ui.view.IONavigation
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    @PerFragment
    internal fun providSplashPresenter(appNavigation: IONavigation, appSession: AppSession): SplashPresenter {
        return SplashPresenter(appNavigation, appSession)
    }

    @Provides
    @PerFragment
    internal fun provideHomePresenter(appNavigation: IONavigation, getBooks: GetBooksUseCase): HomePresenter {
        return HomePresenter(appNavigation, getBooks)
    }

    @Provides
    @PerFragment
    internal fun provideDetailPresenter(appNavigation: IONavigation, getBookDetailUseCase: GetBookDetailUseCase): DetailPresenter {
        return DetailPresenter(appNavigation, getBookDetailUseCase)
    }

    @Provides
    @PerFragment
    internal fun provideCreateBookPresenter(appNavigation: IONavigation, createBookUseCase: CreateBookUseCase, appImagePickerManager: AppImagePickerManager): CreateBookPresenter {
        return CreateBookPresenter(appNavigation, createBookUseCase, appImagePickerManager)
    }
}
