package com.test.books.data.repository

import com.test.books.data.model.Book
import com.test.books.data.utils.RetrofitException
import rx.Observable
import javax.inject.Inject

class MockRepository @Inject
constructor() : Repository {

    override fun getBookList(offset: Int, count: Int): Observable<Array<Book>> {
        return Observable.fromCallable { throw RetrofitException.unexpectedError(null) }
    }

    override fun getBookDetail(id: Int): Observable<Book> {
        return Observable.fromCallable { throw RetrofitException.unexpectedError(null) }
    }

    override fun postBook(book: Book): Observable<Book> {
        return Observable.fromCallable { throw RetrofitException.unexpectedError(null) }
    }
}
