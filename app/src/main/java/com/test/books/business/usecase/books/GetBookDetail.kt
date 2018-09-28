package com.test.books.business.usecase.books

import com.test.books.business.usecase.base.BaseUseCase
import com.test.books.business.usecase.base.BaseUseCaseImpl
import com.test.books.data.model.Book
import com.test.books.data.repository.RepositoryProxy
import rx.Observable
import javax.inject.Inject

interface GetBookDetailUseCase : BaseUseCase<Book> {
    var id: Int
}

class GetBookDetail @Inject constructor(repositoryFactory: RepositoryProxy) : BaseUseCaseImpl<Book>(repositoryFactory), GetBookDetailUseCase {

    override var id: Int = 0

    override fun buildUseCaseObservable(): Observable<Book> {
        return repositoryFactory.repository.getBookDetail(id)
    }
}
