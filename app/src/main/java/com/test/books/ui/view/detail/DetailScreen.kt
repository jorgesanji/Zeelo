package com.test.books.ui.view.detail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.test.books.R
import com.test.books.ui.utils.ImageLoader
import com.test.books.ui.view.base.BaseConstraintLayout
import com.test.books.ui.view.customviews.LoaderView

class DetailScreen(context: Context) : BaseConstraintLayout(context) {

    @BindView(R.id.bookIv)
    protected lateinit var bookIv: ImageView

    @BindView(R.id.bookTitleTv)
    protected lateinit var movieTitleTv: TextView

    @BindView(R.id.bookDescritionTv)
    protected lateinit var movieDescritionTv: TextView

    @BindView(R.id.bookPriceTv)
    protected lateinit var bookPriceTv: TextView

    @BindView(R.id.progress_lv)
    protected lateinit var loaderView: LoaderView

    override val layout: Int get() = R.layout.lay_detail

    override fun initUI(attributeSet: AttributeSet?) {}

    fun setInfo(title: String?, author: String?, price: String?, image: String?) {
        ImageLoader.loadBackground(bookIv, image)
        movieTitleTv.text = title
        movieDescritionTv.text = author
        bookPriceTv.text = price
    }

    fun showLoading() {
        loaderView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loaderView.visibility = View.GONE
    }

}
