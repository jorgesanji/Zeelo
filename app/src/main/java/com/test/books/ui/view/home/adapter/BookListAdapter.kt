package com.test.books.ui.view.home.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.test.books.R
import com.test.books.data.model.Book
import com.test.books.ui.utils.IODataSource

class BookListAdapter(dataSource: IODataSource<Book>) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    private val dataSource: IODataSource<Book>?

    init {
        this.dataSource = dataSource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.lay_book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(dataSource!!.getItemAtPosition(position))
    }

    override fun getItemCount(): Int {
        return dataSource?.count ?: 0
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.titleTv)
        protected lateinit var title_tv: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(book: Book) {
            title_tv.text = book.title
        }
    }
}
