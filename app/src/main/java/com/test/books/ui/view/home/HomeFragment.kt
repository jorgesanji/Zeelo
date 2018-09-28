package com.test.books.ui.view.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.test.books.R
import com.test.books.di.InjectorHelper
import com.test.books.ui.presenter.home.HomePresenter
import com.test.books.ui.view.base.MVPFragment
import com.test.books.ui.view.home.adapter.BookListAdapter

class HomeFragment : MVPFragment<HomePresenter, HomePresenter.View>(), HomePresenter.View, HomeScreen.Listener {

    private lateinit var homeScreen: HomeScreen

    override val rootView: View
        get() {
            homeScreen = HomeScreen(activity)
            homeScreen.listener = this
            return homeScreen
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_add_book ->{presenter.launchCreateBook()}
            android.R.id.home -> {}
        }
        return true
    }

    override fun injectDependencies() {
        InjectorHelper.getPresenterComponent(activity!!).inject(this)
    }

    override fun onDidAppear() {
        presenter.getBooks()
        homeScreen!!.setAdapter(BookListAdapter(presenter))
    }

    /*
    HomePresenter View
    */

    override fun reloadData() {
        homeScreen!!.reloadData()
    }

    /*
     HomeScreen Listener
     */

    override fun onItemClick(view: View, position: Int) {
        presenter.launchDetail(position)
    }

    override fun onLongItemClick(view: View?, position: Int) {}
}