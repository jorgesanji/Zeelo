package com.test.books.data.repository

import com.test.books.data.model.Book
import com.test.books.data.rest.service.RestAdapter
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import java.io.File


class RestRepository(internal var restAdapter: RestAdapter) : Repository {

    override fun getBookList(offset: Int, count: Int): Observable<Array<Book>> {
        return restAdapter.service.getBookList(offset, count)
    }

    override fun getBookDetail(id: Int): Observable<Book> {
        return restAdapter.service.getBookDetail(id)
    }

    override fun postBook(book: Book): Observable<Book> {
        var fbody:RequestBody? = null
        if (book.image != null) {
            val file = File(book.image)
            fbody = RequestBody.create(MediaType.parse("image/*"), file)
        }
        val title = RequestBody.create(MediaType.parse("text/plain"), book.title)
        val author = RequestBody.create(MediaType.parse("text/plain"), book.author)
        val price = RequestBody.create(MediaType.parse("text/plain"), book.price.toString())
        return restAdapter.service.postBook(fbody, title,author,price)
    }
}

