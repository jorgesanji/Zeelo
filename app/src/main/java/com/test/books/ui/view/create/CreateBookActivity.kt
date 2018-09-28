package com.test.books.ui.view.create

import android.os.Bundle
import com.test.books.R
import com.test.books.ui.view.base.BaseActivity

class CreateBookActivity : BaseActivity<CreateBookFragment>() {

    override val fragment: Class<CreateBookFragment>
        get() = CreateBookFragment::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.title_create)
    }

    override fun toolbarColor(): Int {
        return R.color.colorPrimary
    }
}


