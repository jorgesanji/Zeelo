package com.test.books.ui.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object BitmapUtils{

    val IMAGE_MAX_SIZE = 800;

    private fun getResizedBitmap(image: Bitmap): Bitmap {
        var width = image.width
        if (width > IMAGE_MAX_SIZE) {
            var height = image.height

            val bitmapRatio = width.toFloat() / height.toFloat()
            if (bitmapRatio > 1) {
                width = IMAGE_MAX_SIZE
                height = (width / bitmapRatio).toInt()
            } else {
                height = IMAGE_MAX_SIZE
                width = (height * bitmapRatio).toInt()
            }

            return Bitmap.createScaledBitmap(image, width, height, true)
        } else {
            return image
        }
    }

    fun decodeScaledBitmap(filePath: String,
                           reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // formatPhone final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    fun getStream(path: String): ByteArray {
        val mapTostream = BitmapFactory.decodeFile(path)
        val stream = compressBitmap(mapTostream, 100)
        return stream.toByteArray()
    }

    fun compressBitmap(bitmap: Bitmap, quality: Int): ByteArrayOutputStream {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bytes)
        return bytes
    }

    fun compressAndSaveImageOnPath(path: String, quality: Int): String {
        val list = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val idImage = list[list.size - 1]
        val newPath = Environment.getExternalStorageDirectory().toString()
        val file = File(newPath, idImage)
        val bitmap = getResizedBitmap(BitmapFactory.decodeFile(path))
        val bytes = compressBitmap(bitmap, quality)
        try {
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        return file.path
    }

    fun getBase64StringFromBitmap(bitmap: Bitmap?): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }
        return null
    }

    fun getOrientation(path: String): Int {
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)
    }

    fun decodeFromFile(photoPath: String): Bitmap? {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val orientation = getOrientation(photoPath)
        val bitmap = BitmapFactory.decodeFile(photoPath, options)
        return rotateBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.width / 3, bitmap
                .height / 3, false), orientation)
    }

    fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        try {
            val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap
                    .height, matrix, true)
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            return bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            return null
        }

    }

    //TODO: Fixed orientation for samsung devices
    fun fixOrientationIfNeeded(path: String?) {
        try {
            if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
                var bitmapResult: Bitmap? = BitmapFactory.decodeFile(path)
                val exif = ExifInterface(path)
                val orientation = exif
                        .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
                Logger.d("orientation:$orientation")
                val matrix = Matrix()
                var reSave = false
                var scale: Bitmap? = null
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    matrix.postRotate(180f)
                    Logger.d("rotate : 180")
                    scale = Bitmap.createBitmap(bitmapResult!!, 0, 0, bitmapResult.width,
                            bitmapResult.height, matrix, true)
                    reSave = true
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    matrix.postRotate(90f)
                    Logger.d("rotate : 90")
                    scale = Bitmap.createBitmap(bitmapResult!!, 0, 0, bitmapResult.width,
                            bitmapResult.height, matrix, true)
                    reSave = true
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    matrix.postRotate(270f)
                    Logger.d("rotate : 270")
                    scale = Bitmap.createBitmap(bitmapResult!!, 0, 0, bitmapResult.width,
                            bitmapResult.height, matrix, true)
                    reSave = true
                }
                if (reSave) {
                    val bytes = ByteArrayOutputStream()
                    scale!!.compress(Bitmap.CompressFormat.PNG, 0, bytes)
                    val fileOutputStream = FileOutputStream(path)
                    fileOutputStream.write(bytes.toByteArray())
                    fileOutputStream.close()
                }
                var callGC = false
                if (!bitmapResult!!.isRecycled) {
                    bitmapResult.recycle()
                    bitmapResult = null
                    callGC = true
                }
                if (!scale!!.isRecycled) {
                    scale.recycle()
                    scale = null
                    callGC = true
                }
                if (callGC) {
                    System.gc()
                }
            }
        } catch (ex: Exception) {
            Logger.d("FIX_ORIENTATION")
        }
    }
}