package com.test.books.data.local

import android.content.Context
import com.test.books.data.rest.service.OAuthListener
import com.test.books.data.rest.service.RestAdapter
import com.test.books.data.rest.service.SessionUtils
import javax.inject.Inject

interface SessionListener {
    fun onOAuthSuccess()
    fun onErrorObtainingCredentials()
    fun onRefreshSessionFailed()
}

class SessionManager @Inject constructor(private val accountManager:AccountManager, private val restAdapter: RestAdapter, private val context: Context) : OAuthListener, AppSession {

    override var listener: SessionListener? = null

    override fun initSession() {
        val accessToken = accountManager.getAccessToken()
        if (accessToken != null){
            restAdapter.accessToken = accountManager.getAccessToken()
            restAdapter.sessionHandler = this
            listener!!.onOAuthSuccess()
        }else{
            listener!!.onErrorObtainingCredentials()
        }
    }

    override fun getAccessToken(): String? {
        return accountManager.getAccessToken()
    }

    override fun getRefreshToken(): String? {
        return accountManager.getRefreshToken()
    }

    override fun updateSession(validation: SessionUtils.Validation) {
        accountManager.updateAccount(validation)
    }

    override fun onRefreshSessionFailed() {
        restAdapter.accessToken = null
        restAdapter.sessionHandler = null
        listener!!.onRefreshSessionFailed()
    }
}