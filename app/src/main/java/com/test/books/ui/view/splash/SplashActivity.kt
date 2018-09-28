package com.test.books.ui.view.splash

import android.os.Bundle
import android.view.View
import com.test.books.R
import com.test.books.ui.view.base.BaseActivity

class SplashActivity : BaseActivity<SplashFragment>() {

    override val fragment: Class<SplashFragment>
        get() = SplashFragment::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }

    override fun toolbarColor(): Int {
        return R.color.colorPrimary
    }
}


