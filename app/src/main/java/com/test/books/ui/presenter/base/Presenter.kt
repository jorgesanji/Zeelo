package com.test.books.ui.presenter.base

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment

interface Presenter<V : Presenter.View> {

    fun attachView(view: V)

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    fun resume()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    fun pause()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    fun destroy()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onActivityResult() method.
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * Default interface for screen common (most used) operations.
     */
    interface View {
        /**
         * @return The activity.
         */

        val activity : Activity

        /**
         * @return The fragment, if any.
         */
        val fragment: Fragment

        /**
         * Add here your calls to your widget/library to show user info messages.
         *
         * @param message Message box text.
         * @param color
         */
        fun showInfo(message: String, color: Int)

        /**
         * Add here your calls to your widget/library to show user warning messages.
         *
         * @param message Message box text.
         * @param color
         */
        fun showWarning(message: String, color: Int)

        /**
         * Add here your calls to your widget/library to show user error messages.
         *
         * @param message Message box text.
         * @param color
         */
        fun showError(message: String, color: Int)

        /**
         * Shows loading widget/library or another kind of loading view.
         */
        fun showLoading()

        /**
         * Hides loading widget/library or another kind of loading view.
         */
        fun hideLoading()

        /**
         * Sets activity title.
         * Add here your calls to your action bar/tool bar/widget to show the screen title.
         *
         * @param title The title.
         */
        fun setTitle(title: CharSequence)

        /**
         * Sets activity title.
         * Add here your calls to your action bar/tool bar/widget to show the screen title.
         *
         * @param stringResId The title res.
         */
        fun setTitle(stringResId: Int)

        /**
         * Show internet error message.
         */
        fun showInternetConnectionError()

    }
}