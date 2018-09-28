package com.test.books.ui.presenter.splash

import com.test.books.data.local.AppSession
import com.test.books.data.local.SessionListener
import com.test.books.ui.presenter.base.PrensenterImpl
import com.test.books.ui.presenter.base.Presenter
import com.test.books.ui.view.IONavigation

class SplashPresenter(appNavigation: IONavigation, private val appSession: AppSession?) : PrensenterImpl<SplashPresenter.View>(appNavigation) ,SessionListener {

    interface View : Presenter.View {}

    fun initSession(){
        appSession!!.listener = this
        appSession.initSession()
    }

    override fun destroy() {
        super.destroy()
        appSession!!.listener = null
    }

    /**
     * Exist a valid account session on the app
     */
    override fun onOAuthSuccess() {
        appNavigation.launchHome()
        appSession!!.listener = null
    }

    /**
     *  SessionListener
     *
     *  Note: The session always is valid only for the test in a real case sometimes it should'n valid
     */

    /**
     *  If initSession fails it should be redirected to login screen no to the home screen
     */
    override fun onErrorObtainingCredentials() {
        appSession!!.listener = null
    }

    /**
     * If something refresh token is expired the app should logout
     */
    override fun onRefreshSessionFailed() {
        appSession!!.listener = null
    }
}
