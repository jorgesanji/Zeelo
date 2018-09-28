package com.test.books.business.usecase.base

import android.content.Context
import com.test.books.data.rest.service.ApiError
import com.test.books.data.utils.RetrofitException
import rx.Subscriber

abstract class BaseSubscriber<K> : Subscriber<K> {

    private val context: Context?

    constructor(context: Context?) {
        this.context = context
    }

    constructor() {
        this.context = null
    }

    override fun onCompleted() {}

    override fun onError(throwable: Throwable?) {
        try {
            if (throwable != null) {
                val error = throwable as RetrofitException?
                val apiError = error!!.getErrorBodyAs<ApiError>(error)
                onError(apiError ?: ApiError.defaultError)
            } else {
                onError(ApiError.defaultError)
            }
        } catch (e: Exception) {
            //TODO handle other errors
            e.printStackTrace()
            onError(ApiError.defaultError)
        }

    }

    open fun onError(apiError: ApiError) {}

    override fun onNext(k: K) {}
}
