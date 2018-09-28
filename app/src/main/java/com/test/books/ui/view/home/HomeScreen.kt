package com.test.books.ui.view.home

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import butterknife.BindView
import com.test.books.R
import com.test.books.ui.utils.RecyclerItemClickListener
import com.test.books.ui.view.base.BaseLinearLayout

class HomeScreen(context: Context) : BaseLinearLayout(context) {

    interface Listener : RecyclerItemClickListener.OnItemClickListener

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: RecyclerView.Adapter<*>

    var listener: Listener? = null
        set(value) {
            field = value
            mlist_rv!!.addOnItemTouchListener(RecyclerItemClickListener(context, mlist_rv, listener))
        }

    @BindView(R.id.list_rv)
    lateinit var mlist_rv: RecyclerView

    override val layout: Int
        get() = R.layout.lay_home

    override fun initUI(attributeSet: AttributeSet?) {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER or Gravity.TOP
        initLayoutManager()
    }

    private fun initLayoutManager() {
        this.mLayoutManager = LinearLayoutManager(context)
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        mlist_rv!!.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(mlist_rv!!.context,
                mLayoutManager!!.orientation)
        mlist_rv!!.addItemDecoration(dividerItemDecoration)
    }

    fun reloadData() {
        if (mAdapter != null) {
            mAdapter!!.notifyDataSetChanged()
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        this.mAdapter = adapter
        mlist_rv!!.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
    }
}
