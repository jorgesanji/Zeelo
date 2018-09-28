package com.test.books.ui.view.base

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.books.R
import com.test.books.ui.presenter.base.Presenter

abstract class BaseFragment : Fragment(), Presenter.View {

    internal var readyInitialized = false

    override val activity: Activity
        get() = getActivity() as FragmentActivity

    protected abstract val rootView: View

    protected abstract fun onDidAppear()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (!readyInitialized) {
            onDidAppear()
            readyInitialized = true
        }
    }

    override fun showInfo(message: String, color: Int) {
        if (activity is Presenter.View) {
            (activity as Presenter.View).showInfo(message, color)
        } else {
            makeBar(message, if (color == 0) android.R.color.holo_green_light else color)
        }
    }

    override fun showWarning(message: String, color: Int) {
        if (activity is Presenter.View) {
            (activity as Presenter.View).showWarning(message, color)
        } else {
            makeBar(message, if (color == 0) android.R.color.darker_gray else color)
        }
    }

    override fun showError(message: String, color: Int) {
        if (activity is Presenter.View) {
            (activity as Presenter.View).showError(message, color)
        } else {
            makeBar(message, if (color == 0) android.R.color.holo_red_dark else color)
        }
    }

    override fun showInternetConnectionError() {
        showError(getString(R.string.error_internet_connection), 0)
    }

    private fun makeBar(text: String, color: Int) {
        val snackbar = Snackbar.make(view!!, text, Snackbar.LENGTH_LONG)
        val group = snackbar.view as ViewGroup
        group.setBackgroundColor(resources.getColor(color))
        snackbar.show()
    }

    override fun showLoading() {
        (activity as BaseActivity<*>).showLoading()
    }

    override fun hideLoading() {
        (activity as BaseActivity<*>).hideLoading()
    }

    override fun setTitle(title: CharSequence) {
        activity!!.title = title
    }

    override fun setTitle(stringResId: Int) {
        activity!!.setTitle(stringResId)
    }
}
