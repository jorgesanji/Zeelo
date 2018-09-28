package com.test.books.ui.utils

import timber.log.Timber

object Logger{

    @JvmStatic inline fun d(message: String?) = Timber.d(Throwable(message))

}