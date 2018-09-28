package com.test.books.ui.presenter.detail

import com.test.books.business.usecase.base.BaseSubscriber
import com.test.books.business.usecase.books.GetBookDetailUseCase
import com.test.books.data.model.Book
import com.test.books.data.rest.service.ApiError
import com.test.books.ui.presenter.base.PrensenterImpl
import com.test.books.ui.presenter.base.Presenter
import com.test.books.ui.utils.BundleConstants.bookIdKey
import com.test.books.ui.utils.BundleConstants.bookNameKey
import com.test.books.ui.view.IONavigation
import java.text.NumberFormat
import java.util.*

class DetailPresenter(appNavigation: IONavigation, private val getBookDetailUseCase: GetBookDetailUseCase) : PrensenterImpl<DetailPresenter.View>(appNavigation) {

    interface View : Presenter.View {
        fun setInfo(title:String?, author:String?, price: String?, image:String?)
    }

    fun getBookDetail(){
        view.showLoading()
        val bookId = view.fragment.activity!!.intent.extras[bookIdKey] as? Int
        val bookName = view.fragment.activity!!.intent.extras[bookNameKey] as? String
        view.activity.setTitle(bookName)
        getBookDetailUseCase.id = bookId!!
        getBookDetailUseCase.subscribe(object : BaseSubscriber<Book>(){

            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                this@DetailPresenter.view.hideLoading()
                this@DetailPresenter.view.showInternetConnectionError()
            }

            override fun onNext(book: Book) {
                super.onNext(book)
                val currency = Currency.getInstance(Locale.getDefault())
                val value = String.format("%s%s", NumberFormat.getInstance(Locale.getDefault()).format(book.price), currency.symbol)
                this@DetailPresenter.view.setInfo(book.title, book.author, value, book.image)
                this@DetailPresenter.view.hideLoading()
            }
        })
    }
}
