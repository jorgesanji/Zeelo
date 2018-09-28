package com.test.books.ui.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair

import com.test.books.R
import kotlin.reflect.KClass

open class Navigation(protected var activityScope: Activity) {

    companion object {
        val ANIMATION_ACTIVITY = "animation_activity"
    }

    enum class ActivityAnimation private constructor(anim_in: Pair<*, *>, anim_out: Pair<*, *>) {
        SLIDE_UP(Pair(R.anim.anim_slide_in_up, R.anim.anim_hold), Pair(R.anim.anim_hold, R
                .anim.anim_slide_out_down)),
        SLIDE_LEFT(Pair(R.anim.anim_slide_in_left, R.anim.anim_hold), Pair(R.anim
                .anim_hold, R.anim.anim_slide_out_left)),
        SLIDE_RIGHT(Pair(R.anim.anim_slide_in_right, R.anim.anim_hold), Pair(R.anim
                .anim_hold, R.anim.anim_slide_out_right)),
        FADE(Pair(R.anim.fade_in_fast, R.anim.fade_out_fast), Pair(R.anim
                .fade_in_fast, R.anim.fade_out_fast));

        val anim_in: Pair<Int, Int>
        val anim_out: Pair<Int, Int>

        init {
            this.anim_in = anim_in as Pair<Int, Int>
            this.anim_out = anim_out as Pair<Int, Int>
        }
    }

    // ---------------------------- LAUNCH INTENT -------------------------------

    protected fun addAnimation(intent: Intent, animation: ActivityAnimation): Intent {
        var bundle = intent.extras
        if (bundle == null) {
            bundle = Bundle()
        }
        bundle.putSerializable(ANIMATION_ACTIVITY, animation)
        intent.putExtras(bundle)
        return intent
    }

    protected fun startActivity(enterAnim: Int, exitAnim: Int, intent: Intent) {
        activityScope.startActivity(intent)
        activityScope.overridePendingTransition(enterAnim, exitAnim)
    }

    @JvmOverloads
    protected fun startActivity(intent: Intent, animation: ActivityAnimation = ActivityAnimation.FADE) {
        startActivity(animation.anim_in.first!!, animation.anim_in.second!!, addAnimation(intent, animation))
    }

    protected fun startActivityForResult(enterAnim: Int, exitAnim: Int, intent: Intent, code: Int) {
        activityScope.startActivityForResult(intent, code)
        activityScope.overridePendingTransition(enterAnim, exitAnim)
    }

    protected fun startActivityForResult(intent: Intent, code: Int, animation: ActivityAnimation) {
        startActivityForResult(animation.anim_in.first!!, animation.anim_in.second!!,
                addAnimation(intent, animation), code)
    }

    protected fun startActivityForResult(fragment: Fragment, enterAnim: Int, exitAnim: Int, intent: Intent, code: Int) {
        fragment.startActivityForResult(intent, code)
        activityScope.overridePendingTransition(enterAnim, exitAnim)
    }

    // ---------------------------- CREATE INTENT -------------------------------

    protected fun newTask(clazz: KClass<*>, bundle: Bundle?): Intent {
        return newTask(clazz, bundle, false)
    }

    @JvmOverloads
    protected fun newTask(clazz: KClass<*>, bundle: Bundle?, clearTop: Boolean, clearTask: Boolean = false): Intent {
        val openIntent = Intent(activityScope, clazz.java)
        if (bundle != null) {
            openIntent.putExtras(bundle)
        }
        if (clearTop) {
            openIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            openIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            ActivityCompat.finishAffinity(activityScope)
        }
        if (clearTask) {
            openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return openIntent
    }
}
