package com.test.books.ui.view.create

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ImageView
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnTextChanged
import com.test.books.R
import com.test.books.ui.utils.ImageLoader
import com.test.books.ui.view.base.BaseConstraintLayout

class CreateBookScreen(context: Context) : BaseConstraintLayout(context) {

    interface Listener {
        fun setTitle(title:String?)
        fun setAuthor(author:String?)
        fun setPrice(price:String?)
        fun cameraPressed()
        fun galleryPressed()
    }

    lateinit var listener: Listener

    @BindView(R.id.book_iv)
    protected lateinit var bookIv:ImageView
    @BindView(R.id.book_title_il)
    protected lateinit var titleIl: TextInputLayout
    @BindView(R.id.book_author_il)
    protected lateinit var authorIl: TextInputLayout
    @BindView(R.id.book_price_il)
    protected lateinit var priceIl: TextInputLayout

    override val layout: Int
        get() = R.layout.lay_createbook

    override fun initUI(attributeSet: AttributeSet?) {
    }

    @OnClick(R.id.camera_bt)
    protected fun cameraPressed() {
        val mCameraButton = findViewById<ImageButton>(R.id.camera_bt)
        val popup = PopupMenu(context, mCameraButton)
        popup.menuInflater.inflate(R.menu.camera_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_camera -> listener.cameraPressed()
                R.id.action_gallery -> listener.galleryPressed()
            }
            true
        }
        popup.show()
    }

    @OnTextChanged(R.id.book_title_ed, callback = OnTextChanged.Callback.TEXT_CHANGED)
    fun changeTitleInput(charSequence: CharSequence) {
        listener.setTitle(charSequence.toString())
    }

    @OnTextChanged(R.id.book_author_ed, callback = OnTextChanged.Callback.TEXT_CHANGED)
    fun changeAuthorInput(charSequence: CharSequence) {
        listener.setAuthor(charSequence.toString())
    }

    @OnTextChanged(R.id.book_price_ed, callback = OnTextChanged.Callback.TEXT_CHANGED)
    fun changePriceInput(charSequence: CharSequence) {
        listener.setPrice(charSequence.toString())
    }

    fun showTitleError() {
        titleIl.error = context.getString(R.string.create_book_title_error)
    }

    fun showAuthorError() {
        authorIl.error = context.getString(R.string.create_book_author_error)
    }

    fun showPriceError() {
        priceIl.error = context.getString(R.string.create_book_price_error)
    }

    fun hideTitleError() {
        titleIl.error = null
    }

    fun hideAuthorError() {
        authorIl.error = null
    }

    fun hidePriceError() {
        priceIl.error = null
    }

    fun setBookImage(path: String?) {
        ImageLoader.loadImageFromPath(bookIv, path)
    }
}
