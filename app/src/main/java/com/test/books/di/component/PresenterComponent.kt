package com.test.books.di.component

import com.test.books.di.PerFragment
import com.test.books.di.module.ManagerModule
import com.test.books.di.module.NavigatorModule
import com.test.books.di.module.PresenterModule
import com.test.books.di.module.UseCaseModule
import com.test.books.ui.view.create.CreateBookFragment
import com.test.books.ui.view.detail.DetailFragment
import com.test.books.ui.view.home.HomeFragment
import com.test.books.ui.view.splash.SplashFragment
import dagger.Component

@PerFragment
@Component(modules = arrayOf(PresenterModule::class, NavigatorModule::class, UseCaseModule::class, ManagerModule::class), dependencies = arrayOf(ApplicationComponent::class))
interface PresenterComponent {

    fun inject(splashFragment: SplashFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(detailFragment: DetailFragment)
    fun inject(createBookFragment: CreateBookFragment)

}
