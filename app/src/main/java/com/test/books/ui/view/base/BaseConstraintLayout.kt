package com.test.books.ui.view.base

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import butterknife.ButterKnife

abstract class BaseConstraintLayout : ConstraintLayout, BaserView {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    protected fun init(attrs: AttributeSet?) {
        val layout = layout
        if (layout != 0) {
            View.inflate(context, layout, this)
            ButterKnife.bind(this)
        }
        setBackgroundResource(android.R.color.white)
        initUI(attrs)
    }
}
