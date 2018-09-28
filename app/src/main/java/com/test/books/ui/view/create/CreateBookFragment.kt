package com.test.books.ui.view.create

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.test.books.R
import com.test.books.di.InjectorHelper
import com.test.books.ui.presenter.create.CreateBookPresenter
import com.test.books.ui.view.base.MVPFragment

class CreateBookFragment : MVPFragment<CreateBookPresenter, CreateBookPresenter.View>(), CreateBookPresenter.View, CreateBookScreen.Listener{

    private lateinit var createBookScreen:CreateBookScreen

    override val rootView: View get(){
        createBookScreen = CreateBookScreen(activity)
        createBookScreen.listener = this
        return createBookScreen
    }

    override fun injectDependencies() {
        InjectorHelper.getPresenterComponent(activity).inject(this)
    }

    override fun onDidAppear() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.create_book_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_done ->{presenter.createBook()}
            android.R.id.home -> {}
        }
        return true
    }

    /**
     *   CreateBookPresenter.View
     */

    override fun showTitleError() {
        createBookScreen.showTitleError()
    }

    override fun showAuthorError() {
        createBookScreen.showAuthorError()
    }

    override fun showPriceError() {
        createBookScreen.showPriceError()
    }

    override fun setBookImage(path: String?) {
        createBookScreen.setBookImage(path)
    }

    override fun hideTitleError() {
        createBookScreen.hideTitleError()
    }

    override fun hideAuthorError() {
        createBookScreen.hideAuthorError()
    }

    override fun hidePriceError() {
        createBookScreen.hidePriceError()
    }

    override fun showCreateBookSuccessMessage() {
        Toast.makeText(context, context!!.getString(R.string.create_book_success), Toast.LENGTH_LONG).show()
        activity.finish()
    }

    /**
     *   CreateBookScreen.Listener
     */

    override fun cameraPressed() {
        presenter.launchCamera()
    }

    override fun galleryPressed() {
        presenter.launchGallery()
    }

    override fun setTitle(title: String?) {
        presenter.title = title
    }

    override fun setAuthor(author: String?) {
        presenter.author = author
    }

    override fun setPrice(price: String?) {
        presenter.price = price
    }
}