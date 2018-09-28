package com.test.books.ui.utils

import android.graphics.*

import com.squareup.picasso.Transformation

class RoundCornersTransform(private val round: Boolean) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)
        val x = ((source.width - size) / TWO).toInt()
        val y = ((source.height - size) / TWO).toInt()
        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        if (round) {
            val radius = size / TWO
            canvas.drawCircle(radius, radius, radius, paint)
        } else {
            val rect = RectF()
            rect.set(0f, 0f, squaredBitmap.width.toFloat(), squaredBitmap.height.toFloat())
            canvas.drawRoundRect(rect, RADIUS, RADIUS, paint)
        }

        squaredBitmap.recycle()

        return bitmap
    }

    override fun key(): String {
        return RoundCornersTransform::class.java!!.getName()
    }

    companion object {

        private val TWO = 2f
        val RADIUS = 20f
    }
}