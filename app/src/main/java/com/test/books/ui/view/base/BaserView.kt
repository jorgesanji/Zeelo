package com.test.books.ui.view.base

import android.util.AttributeSet

interface BaserView {

    val layout: Int

    fun initUI(attributeSet: AttributeSet?)
}
