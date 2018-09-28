package com.test.books.ui.presenter.home

import android.os.Bundle
import com.test.books.business.usecase.base.BaseSubscriber
import com.test.books.business.usecase.books.GetBooksUseCase
import com.test.books.data.model.Book
import com.test.books.data.rest.service.ApiError
import com.test.books.ui.presenter.base.PrensenterImpl
import com.test.books.ui.presenter.base.Presenter
import com.test.books.ui.utils.BundleConstants.bookIdKey
import com.test.books.ui.utils.BundleConstants.bookNameKey
import com.test.books.ui.utils.IODataSource
import com.test.books.ui.view.IONavigation

class HomePresenter(appNavigation: IONavigation, private var getBooks: GetBooksUseCase) : PrensenterImpl<HomePresenter.View>(appNavigation), IODataSource<Book> {

    private var books: List<Book> = ArrayList()
    private var loading: Boolean = false
    private val sizePage = 10
    private var offset = 0
    private var stopLoad = false

    interface View : Presenter.View {
        fun reloadData()
    }

    private fun retrieveMoreDataIfNeeded(at: Int) {
        if (at >= (books.size - sizePage / 2) && !loading && !stopLoad) {
            getBooks()
        }
    }

    fun getBooks() {
        if (books.isEmpty()) {
            view.showLoading()
        }
        loading = true
        getBooks.offset = offset
        getBooks.count = sizePage
        getBooks.subscribe(object : BaseSubscriber<Array<Book>>() {

            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                this@HomePresenter.view.hideLoading()
                this@HomePresenter.view.showInternetConnectionError()
                this@HomePresenter.loading = false
            }

            override fun onNext(response: Array<Book>) {
                super.onNext(response)
                this@HomePresenter.stopLoad = response.size == 0
                var result = books + response
                this@HomePresenter.books = result
                this@HomePresenter.offset = result.size
                this@HomePresenter.view.reloadData()
                this@HomePresenter.view.hideLoading()
                this@HomePresenter.loading = false
            }
        })
    }

    fun launchDetail(position: Int){
        val book = books[position]
        var bundle = Bundle()
        bundle.putInt(bookIdKey, book.id!!)
        bundle.putString(bookNameKey, book.title!!)
        appNavigation.launchDetail(bundle)
    }

    fun launchCreateBook(){
        appNavigation.launchCreateBook(null)
    }

    /**
     * Datasource
     */

    override val count: Int
        get() = books.size

    override fun getItemAtPosition(position: Int): Book {
        retrieveMoreDataIfNeeded(position)
        return books[position]
    }
}
