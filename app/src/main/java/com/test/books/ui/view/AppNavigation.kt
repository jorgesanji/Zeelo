package com.test.books.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.test.books.ui.utils.Navigation
import com.test.books.ui.view.create.CreateBookActivity
import com.test.books.ui.view.detail.DetailActivity
import com.test.books.ui.view.home.HomeActivity
import javax.inject.Inject

interface IONavigation{
    fun launchHome()
    fun launchDetail(bundle:Bundle?)
    fun launchCreateBook(bundle:Bundle?)
}

class AppNavigation @Inject constructor(activity: Activity) : Navigation(activity), IONavigation{

    // ****************************
    //      INTENTS CREATION
    // ****************************

    private fun home(): Intent {
        return newTask(HomeActivity::class!!, null, true)
    }

    private fun detail(bundle:Bundle?): Intent {
        return newTask(DetailActivity::class!!, bundle)
    }

    private fun createBook(bundle:Bundle?): Intent {
        return newTask(CreateBookActivity::class!!, bundle)
    }

    // ****************************
    //      ACTIONS DEFINITION
    // ****************************

    override fun launchHome() {
        startActivity(home())
    }

    override fun launchDetail(bundle:Bundle?) {
        startActivity(detail(bundle), ActivityAnimation.SLIDE_LEFT)
    }

    override fun launchCreateBook(bundle: Bundle?) {
        startActivity(createBook(bundle), ActivityAnimation.SLIDE_UP)
    }
}
