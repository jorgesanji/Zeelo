package com.test.books.data.local

interface AppSession{
    var listener:SessionListener?
    fun initSession()
}