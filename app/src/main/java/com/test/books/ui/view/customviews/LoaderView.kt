package com.test.books.ui.view.customviews

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ProgressBar

import com.test.books.R

class LoaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ProgressBar(context, attrs, defStyleAttr) {

    private var animator: Animator? = null

    private val isStarted: Boolean
        get() = animator != null && animator!!.isStarted

    init {
        val backgroundColour: Int
        val progressColour: Int
        backgroundColour = ContextCompat.getColor(context, R.color.pale_gray)
        progressColour = ContextCompat.getColor(context, R.color.pine_green)
        val resources = context.resources
        progressDrawable = resources.getDrawable(android.R.drawable.progress_horizontal)
        createIndeterminateProgressDrawable(backgroundColour, progressColour)
        max = INDETERMINATE_MAX
        super.setIndeterminate(false)
        this.isIndeterminate = true
    }

    private fun createIndeterminateProgressDrawable(@ColorInt backgroundColour: Int, @ColorInt
    progressColour: Int) {
        val layerDrawable = progressDrawable as LayerDrawable
        if (layerDrawable != null) {
            layerDrawable.mutate()
            layerDrawable.setDrawableByLayerId(android.R.id.background, createShapeDrawable(backgroundColour))
            layerDrawable.setDrawableByLayerId(android.R.id.progress, createClipDrawable(backgroundColour))
            layerDrawable.setDrawableByLayerId(android.R.id.secondaryProgress, createClipDrawable(progressColour))
        }
    }

    private fun createClipDrawable(@ColorInt colour: Int): Drawable {
        val shapeDrawable = createShapeDrawable(colour)
        return ClipDrawable(shapeDrawable, Gravity.START, ClipDrawable.HORIZONTAL)
    }

    private fun createShapeDrawable(@ColorInt colour: Int): ShapeDrawable {
        val shapeDrawable = ShapeDrawable()
        setColour(shapeDrawable, colour)
        return shapeDrawable
    }

    private fun setColour(drawable: ShapeDrawable, colour: Int) {
        val paint = drawable.paint
        paint.color = colour
    }

    @Synchronized
    override fun setIndeterminate(indeterminate: Boolean) {
        if (isStarted) {
            return
        }
        animator = createIndeterminateAnimator()
        animator!!.setTarget(this)
        animator!!.start()
    }

    private fun createIndeterminateAnimator(): Animator {
        val set = AnimatorSet()
        val progressAnimator = getAnimator(SECONDARY_PROGRESS, DecelerateInterpolator())
        val secondaryProgressAnimator = getAnimator(PROGRESS, AccelerateInterpolator())
        set.playTogether(progressAnimator, secondaryProgressAnimator)
        set.duration = DURATION.toLong()
        return set
    }

    private fun getAnimator(propertyName: String, interpolator: Interpolator): ObjectAnimator {
        val progressAnimator = ObjectAnimator.ofInt(this, propertyName, 0,
                INDETERMINATE_MAX)
        progressAnimator.interpolator = interpolator
        progressAnimator.duration = DURATION.toLong()
        progressAnimator.repeatMode = ValueAnimator.RESTART
        progressAnimator.repeatCount = ValueAnimator.INFINITE
        return progressAnimator
    }

    companion object {

        private val INDETERMINATE_MAX = 6000
        private val SECONDARY_PROGRESS = "secondaryProgress"
        private val DURATION = 1500
        private val PROGRESS = "progress"
    }
}