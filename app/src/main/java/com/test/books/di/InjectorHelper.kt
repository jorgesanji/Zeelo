package com.test.books.di

import android.app.Activity

import com.test.books.di.component.DaggerPresenterComponent
import com.test.books.di.component.PresenterComponent
import com.test.books.di.module.ManagerModule
import com.test.books.di.module.NavigatorModule
import com.test.books.di.module.PresenterModule
import com.test.books.di.module.UseCaseModule
import com.test.books.ui.application.SampleApplication

object InjectorHelper {

    fun getPresenterComponent(activity: Activity): PresenterComponent {
        return DaggerPresenterComponent.builder()
                .applicationComponent(SampleApplication.instance!!
                        .applicationComponent)
                .useCaseModule(UseCaseModule(activity))
                .navigatorModule(NavigatorModule(activity))
                .presenterModule(PresenterModule())
                .managerModule(ManagerModule(activity))
                .build()
    }
}
