package com.test.books.ui.view.home

import android.os.Bundle

import com.test.books.R
import com.test.books.ui.view.base.BaseActivity

class HomeActivity : BaseActivity<HomeFragment>() {

    override val fragment: Class<HomeFragment>
        get() = HomeFragment::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher)
        title = getString(R.string.title_home)
    }

    override fun toolbarColor(): Int {
        return R.color.colorPrimary
    }

    override fun centerTitle(): Boolean {
        return false
    }
}
