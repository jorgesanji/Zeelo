package com.test.books.business.usecase.books

import com.test.books.business.usecase.base.BaseUseCase
import com.test.books.business.usecase.base.BaseUseCaseImpl
import com.test.books.data.model.Book
import com.test.books.data.repository.RepositoryProxy
import rx.Observable
import javax.inject.Inject

interface GetBooksUseCase : BaseUseCase<Array<Book>> {
    var offset: Int
    var count : Int
}

class GetBooks @Inject constructor(repositoryFactory: RepositoryProxy) : BaseUseCaseImpl<Array<Book>>(repositoryFactory), GetBooksUseCase {

    override var offset: Int = 0
    override var count: Int = 0

    override fun buildUseCaseObservable(): Observable<Array<Book>> {
        return repositoryFactory.repository.getBookList(offset, count)
    }
}
