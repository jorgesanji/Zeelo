package com.test.books.integration

import android.app.Application
import com.test.books.business.usecase.base.BaseSubscriber
import com.test.books.business.usecase.books.CreateBook
import com.test.books.business.usecase.books.CreateBookUseCase
import com.test.books.data.model.Book
import com.test.books.data.repository.LocalRepository
import com.test.books.data.repository.MockRepository
import com.test.books.data.repository.RestRepository
import com.test.books.data.rest.service.ApiError
import com.test.books.data.rest.service.RestAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateBookntegrationTest {

    val accessToken = "accessToken"
    @Mock
    lateinit var mMockContext: Application

    lateinit var repositoryProxy: RepositoryProxyTest
    lateinit var createBookUseCase: CreateBookUseCase

    @Before
    fun initUseCases() {
        val restAdapter = RestAdapter()
        restAdapter.accessToken = accessToken
        val restRepository = RestRepository(restAdapter)
        val localRepository = LocalRepository()
        val mockRepository = MockRepository()
        this.repositoryProxy = RepositoryProxyTest(mMockContext, restRepository,
                localRepository, mockRepository)
        this.createBookUseCase = CreateBook(repositoryProxy)
    }

    @Test
    fun postBookWithNoImage() {
        val book = Book()
        book.title = "titleTest"
        book.author = "authorTest"
        book.price = 1.0
        createBookUseCase.book = book
        createBookUseCase.subscribeTest(object : BaseSubscriber<Book>() {
            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                assert(apiError != null)
            }

            override fun onNext(response:Book) {
                super.onNext(response)
                assert(response!!.id != null){"book no valid"}
            }
        })
    }

    @Test
    fun postBookWithErroneusImagePathTest() {
        val book = Book()
        book.title = "titleTest"
        book.author = "authorTest"
        book.price = 1.0
        book.image = "image"
        createBookUseCase.book = book
        createBookUseCase.subscribeTest(object : BaseSubscriber<Book>() {
            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                assert(apiError != null)
            }

            override fun onNext(response:Book) {
                super.onNext(response)
                assert(response!!.id != null){"book no valid"}
            }
        })
    }
}

