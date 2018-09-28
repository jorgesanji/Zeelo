package com.test.books.data.utils

import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.HttpException
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {

    private val original: RxJavaCallAdapterFactory

    init {
        original = RxJavaCallAdapterFactory.create()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*> {
        return if (CallAdapter.Factory.getRawType(returnType) == Observable::class.java) {
            RxObservableCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit))
        } else {
            original.get(returnType, annotations, retrofit)
        }
    }

    private class RxObservableCallAdapterWrapper(private val retrofit: Retrofit, private val wrapped: CallAdapter<*>) : CallAdapter<Observable<*>> {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun <R> adapt(call: Call<R>): Observable<*> {
            return (wrapped.adapt(call) as Observable<*>).onErrorResumeNext { throwable: Throwable ->
                Observable.error(asRetrofitException(throwable))
            }
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException? {
            // We had non-200 http error
            try {
                if (throwable is HttpException) {
                    val response = throwable.response()
                    return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit)
                }
                if (throwable is JsonSyntaxException) {
                    return RetrofitException.httpError("JSON EXCEPTION", null!!, retrofit)
                }
                // A network error happened
                if (throwable is IOException) {
                    return RetrofitException.networkError(throwable)
                }
                // We don't know what happened. We need to simply convert to an unknown error
                if (throwable is IllegalStateException) {
                    return RetrofitException.illegalStateError(throwable)
                }
            } catch (e: Exception) {
                return RetrofitException.unexpectedError(throwable)
            }
            return null
        }
    }

    companion object {

        fun create(): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory()
        }
    }
}

