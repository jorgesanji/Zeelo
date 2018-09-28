package com.test.books.data.repository

import com.test.books.data.model.Book
import rx.Observable

interface Repository {
    fun getBookList(offset:Int, count:Int) : Observable<Array<Book>>
    fun getBookDetail(id:Int) : Observable<Book>
    fun postBook(book:Book) : Observable<Book>
}
