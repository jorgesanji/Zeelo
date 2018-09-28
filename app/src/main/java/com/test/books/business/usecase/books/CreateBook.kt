package com.test.books.business.usecase.books

import com.test.books.business.usecase.base.BaseUseCase
import com.test.books.business.usecase.base.BaseUseCaseImpl
import com.test.books.data.model.Book
import com.test.books.data.repository.RepositoryProxy
import rx.Observable
import javax.inject.Inject

interface CreateBookUseCase : BaseUseCase<Book> {
    var book: Book
}

class CreateBook @Inject constructor(repositoryFactory: RepositoryProxy) : BaseUseCaseImpl<Book>(repositoryFactory), CreateBookUseCase {

    override lateinit var book: Book

    override fun buildUseCaseObservable(): Observable<Book> {
        return repositoryFactory.repository.postBook(book)
    }
}
