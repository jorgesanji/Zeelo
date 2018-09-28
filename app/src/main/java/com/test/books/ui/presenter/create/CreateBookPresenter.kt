package com.test.books.ui.presenter.create

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.test.books.business.usecase.base.BaseSubscriber
import com.test.books.business.usecase.books.CreateBookUseCase
import com.test.books.data.model.Book
import com.test.books.data.rest.service.ApiError
import com.test.books.ui.presenter.base.PrensenterImpl
import com.test.books.ui.presenter.base.Presenter
import com.test.books.ui.utils.AppImagePickerManager
import com.test.books.ui.utils.IOPickerImageSelector
import com.test.books.ui.view.IONavigation


class CreateBookPresenter(appNavigation: IONavigation,
                          private val createBookUseCase: CreateBookUseCase,
                          private val appImagePickerManager: AppImagePickerManager)
    : PrensenterImpl<CreateBookPresenter.View>(appNavigation), IOPickerImageSelector {

    interface View : Presenter.View {
        fun showTitleError()
        fun showAuthorError()
        fun showPriceError()
        fun hideTitleError()
        fun hideAuthorError()
        fun hidePriceError()
        fun setBookImage(path: String?)
        fun showCreateBookSuccessMessage()
    }

    var title: String? = null
        set(value) {
            field = value
            if (value != null && !value.isEmpty()) {
                view.hideTitleError()
            }
        }
    var author: String? = null
        set(value) {
            field = value
            if (value != null && !value.isEmpty()) {
                view.hideAuthorError()
            }
        }

    var price: String? = null
        set(value) {
            field = value
            if (value != null && !value.isEmpty()) {
                view.hidePriceError()
            }
        }
    private var imagePath: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        appImagePickerManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun attachView(view: View) {
        super.attachView(view)
        appImagePickerManager.setOwner(view.fragment)
        appImagePickerManager.listener = this
    }

    fun launchCamera() {
        appImagePickerManager.launchCamera()
    }

    fun launchGallery() {
        appImagePickerManager.launchGallery(false)
    }

    fun createBook() {
        val imm = view.activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.activity.getCurrentFocus()!!.getWindowToken(), 0)

        if (title == null) {
            view.showTitleError()
            return
        }
        if (author == null) {
            view.showAuthorError()
            return
        }
        if (price == null) {
            view.showPriceError()
            return
        }
        var book = Book()
        book.title = title
        book.author = author
        book.price = price!!.toDouble()
        book.image = imagePath
        createBookUseCase.book = book
        createBookUseCase.subscribe(object : BaseSubscriber<Book>(){

            override fun onError(apiError: ApiError) {
                super.onError(apiError)
                this@CreateBookPresenter.view.hideLoading()
                this@CreateBookPresenter.view.showInternetConnectionError()
            }

            override fun onNext(book: Book) {
                super.onNext(book)
                this@CreateBookPresenter.view.hideLoading()
                this@CreateBookPresenter.view.showCreateBookSuccessMessage()
            }
        })
    }

    /**
     *  IOPickerImageSelector
     */

    override fun onImageSelected(path: String) {
        this.imagePath = path
        view.setBookImage(imagePath)
    }

    override fun onMultiImagesSelected(images: List<String>) {}

    override fun loading() {}

    override fun onCancel() {}

    override fun onError() {}
}
