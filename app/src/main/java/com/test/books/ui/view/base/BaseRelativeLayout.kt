package com.test.books.ui.view.base

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

import butterknife.ButterKnife

abstract class BaseRelativeLayout : RelativeLayout, BaserView {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
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
