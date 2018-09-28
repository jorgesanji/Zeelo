package com.test.books.data.local

import com.test.books.data.rest.service.SessionUtils
import javax.inject.Inject

interface AccountManager {
    fun createAccount(validation: SessionUtils.Validation)
    fun updateAccount(validation: SessionUtils.Validation)
    fun removeAccount()
    fun setRefreshToken(refreshToken: String)
    fun setAccessToken(accessToken: String)
    fun getRefreshToken(): String?
    fun getAccessToken(): String?
}

/**
 *   Note: Data can be saved on database on account manager for android
 */

class AccountManagerImpl @Inject constructor(): AccountManager{

    /**
     * Create an account
     */
    override fun createAccount(validation: SessionUtils.Validation) {
        removeAccount()
        updateAccount(validation)
    }

    /**
     * Update the current account
     */
    override fun updateAccount(validation: SessionUtils.Validation) {}

    /**
     * Remove current Account
     */
    override fun removeAccount() {}

    /**
     * Update session accessToken
     */
    override fun setRefreshToken(refreshToken: String) {}

    /**
     * Update session refreshToken
     */
    override fun setAccessToken(accessToken: String) {}

    /**
     * Get the current accessToken
     */
    override fun getRefreshToken(): String? {
        return "refreshToken"
    }

    /**
     * Get the current refreshToken
     */
    override fun getAccessToken(): String? {
        return "accessToken"
    }
}