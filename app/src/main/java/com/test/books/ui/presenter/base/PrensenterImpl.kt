package com.test.books.ui.presenter.base

import android.content.Intent
import com.test.books.ui.view.IONavigation

open class PrensenterImpl<V : Presenter.View>(var appNavigation: IONavigation) : Presenter<V> {

    // Views
    protected lateinit var view: V

    override fun attachView(view: V) {
        this.view = view
    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun destroy() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }
}
