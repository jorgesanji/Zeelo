package com.test.books.ui.view.splash

import android.view.View
import com.test.books.di.InjectorHelper
import com.test.books.ui.presenter.splash.SplashPresenter
import com.test.books.ui.view.base.MVPFragment
class SplashFragment : MVPFragment<SplashPresenter, SplashPresenter.View>(){

    override val rootView: View get(){return SplashScreen(activity!!)}

    override fun injectDependencies() {
        InjectorHelper.getPresenterComponent(activity!!).inject(this)
    }

    override fun onDidAppear() {
        presenter.initSession()
    }
}