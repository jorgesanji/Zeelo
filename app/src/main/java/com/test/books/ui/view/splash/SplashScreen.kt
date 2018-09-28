package com.test.books.ui.view.splash

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity

import com.test.books.R
import com.test.books.ui.view.base.BaseLinearLayout

class SplashScreen(context: Context) : BaseLinearLayout(context) {

    override val layout: Int
        get() = R.layout.lay_splash

    override fun initUI(attributeSet: AttributeSet?) {
        gravity = Gravity.CENTER
    }
}
