package com.test.books.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils{

    @Throws(IOException::class)
    fun createImageFile(context: Context, name: String): File {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                name, /* prefix */
                ".jpg", /* suffix */
                directory      /* directory */
        )
    }

    fun saveImage(context: Context, name: String, bitmap: Bitmap): Boolean {
        try {
            val imageFile = ImageUtils.createImageFile(context, name)
            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()
            return true
        } catch (ex: Exception) {
            //Timber.d(ex.message)
        }
        return false
    }
}