package com.test.books.ui.view.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import butterknife.BindView
import butterknife.ButterKnife
import com.test.books.R
import com.test.books.ui.utils.AppPermissionsManagerImpl
import com.test.books.ui.utils.IOAppPermission
import com.test.books.ui.utils.Navigation
import com.test.books.ui.view.customviews.LoaderView
import java.util.*

abstract class BaseActivity<F : MVPFragment<*, *>> : AppCompatActivity() {

    var currentFragment: F? = null
    lateinit var permissionListener: IOAppPermission

    abstract val fragment: Class<F>

    @BindView(com.test.books.R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(com.test.books.R.id.progress_lv)
    lateinit var loaderView: LoaderView

    abstract fun toolbarColor(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.test.books.R.layout.activity_lay)
        ButterKnife.bind(this)
        initFragment()
        initToolBar()
    }

    protected fun initToolBar() {
        setSupportActionBar(toolbar)
        setToolBarBackgroundColor(toolbarColor())
        setStatusColor(ContextCompat.getColor(this, R
                .color.colorPrimary))
    }

    protected open fun centerTitle(): Boolean {
        return false
    }

    protected fun initFragment() {
        this.currentFragment = Fragment.instantiate(this, fragment.name) as F
        if (currentFragment != null) {
            currentFragment!!.setArguments(intent.extras)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(com.test.books.R.id.contentFrame, currentFragment)
            transaction.commit()
        }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun finish() {
        super.finish()
        var activityAnimation: Navigation.ActivityAnimation? = intent.getSerializableExtra(Navigation.ANIMATION_ACTIVITY) as Navigation.ActivityAnimation
        if (activityAnimation == null) {
            activityAnimation = Navigation.ActivityAnimation.FADE
        }
        overridePendingTransition(activityAnimation!!.anim_out.first!!, activityAnimation!!
                .anim_out.second!!)
    }

    override fun setTitle(title: CharSequence) {
        val ab = supportActionBar
        ab!!.title = title
        if (centerTitle()) {
            val window = window
            val decor = window.decorView
            val views = ArrayList<View>()
            decor.findViewsWithText(views, title, View.FIND_VIEWS_WITH_TEXT)
            val tvTitle = views[0] as AppCompatTextView
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            tvTitle.gravity = Gravity.CENTER
            tvTitle.width = metrics.widthPixels
        }
    }

    fun setToolBarBackgroundColor(color: Int) {
        toolbar!!.setBackgroundColor(ContextCompat.getColor(this, color))
    }

    fun showLoading() {
        loaderView!!.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loaderView!!.visibility = View.GONE
    }

    fun setStatusColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = color
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.size > 0) {
            val permission = AppPermissionsManagerImpl.Permission.getPermissionFromCode(requestCode)
            val permissionAccepted = grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (application is IOAppPermission) {
                (application as IOAppPermission).permission(permission, permissionAccepted)
            }
            if (permissionListener != null) {
                permissionListener.permission(permission, permissionAccepted)
            }
        }
    }
}

