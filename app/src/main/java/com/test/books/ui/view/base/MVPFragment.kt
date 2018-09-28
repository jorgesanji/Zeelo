package com.test.books.ui.view.base

import android.content.Intent
import android.os.Bundle
import com.f2prateek.dart.Dart
import com.test.books.ui.presenter.base.PrensenterImpl
import com.test.books.ui.presenter.base.Presenter
import javax.inject.Inject

abstract class MVPFragment<P : PrensenterImpl<V>, V : Presenter.View> : BaseFragment() {

    @Inject
    lateinit var presenter: P

    override val fragment: MVPFragment<P, V>
        get() = this

    protected abstract fun injectDependencies()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        presenter!!.attachView(this as V)
        Dart.inject(presenter!!, activity)
    }

    override fun onPause() {
        super.onPause()
        if (presenter != null) {
            presenter!!.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (presenter != null) {
            presenter!!.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter != null) {
            presenter!!.destroy()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (presenter != null) {
            presenter!!.onActivityResult(requestCode, resultCode, data)
        }
    }
}