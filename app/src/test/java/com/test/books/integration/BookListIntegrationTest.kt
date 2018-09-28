package com.test.books.integration

import android.app.Application
import com.test.books.business.usecase.base.BaseSubscriber
import com.test.books.business.usecase.books.GetBooks
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
class BookListIntegrationTest {

    val accessToken = "accessToken"
    @Mock
    lateinit var mMockContext: Application

    lateinit var repositoryProxy: RepositoryProxyTest
    lateinit var getBooks: GetBooks

    @Before
    fun initUseCases() {
        val restAdapter = RestAdapter()
        restAdapter.accessToken = accessToken
        val restRepository = RestRepository(restAdapter)
        val localRepository = LocalRepository()
        val mockRepository = MockRepository()
        this.repositoryProxy = RepositoryProxyTest(mMockContext, restRepository,
                localRepository, mockRepository)
        this.getBooks = GetBooks(repositoryProxy)
    }

    @Test
    fun getBooksOnePageTest() {
        getBooks.offset = 0
        getBooks.count = 10
        getBooks.subscribeTest(object : BaseSubscriber<Array<Book>>() {
            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                assert(apiError != null)
            }

            override fun onNext(response:Array<Book>) {
                super.onNext(response)
                assert(!response!!.isEmpty()){"No movies retrieved"}
            }
        })
    }

    @Test
    fun getBooksZeroPageTest() {
        getBooks.offset = 0
        getBooks.count = 0
        getBooks.subscribeTest(object : BaseSubscriber<Array<Book>>() {
            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                assert(apiError != null){"Page should be major than 0"}
            }
        })
    }

    @Test
    fun getBooksPaginationTest() {
        var pagination = true
        var offset = 0
        var pages = 0
        var books:List<Book> = ArrayList<Book>()
        getBooks.count = 10
        while(pagination){
            pages++
            getBooks.offset = offset
            getBooks.subscribeTest(object : BaseSubscriber<Array<Book>>() {
                override fun onError(apiError: ApiError) {
                    super.onError(apiError)
                    pagination = false
                    assert(apiError != null)
                }

                override fun onNext(response: Array<Book>) {
                    super.onNext(response)
                    assert(!response!!.isEmpty()){"No movies retrieved"}
                    var result = books + response
                    books = result
                }
            })
            offset = (offset + 10) * pages
        }
        assert(!books!!.isEmpty()){"No movies retrieved"}
    }
}

