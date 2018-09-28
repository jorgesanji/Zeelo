package com.test.books.data.rest.service

import com.google.gson.annotations.SerializedName
import com.test.books.data.rest.service.RestConstants.REFRESH_TOKEN_INVALID

class ApiError {

    @SerializedName("statusCode")
    protected var statusCode: Int? = 0

    @SerializedName("status_message")
    protected var message: String? = null

    fun setMessage(message: String): ApiError {
        this.message = message
        return this
    }

    fun setStatusCode(code: Int): ApiError {
        this.statusCode = code
        return this
    }

    val  isRefreshTokenExpired = statusCode == REFRESH_TOKEN_INVALID

    companion object {

        val API_ERROR_UNDEFINED = 0x01
        val API_ERROR_MESSAGE = "UNDEFINED ERROR"

        val defaultError: ApiError
            get() = ApiError().setStatusCode(API_ERROR_UNDEFINED).setMessage(API_ERROR_MESSAGE)
    }
}
