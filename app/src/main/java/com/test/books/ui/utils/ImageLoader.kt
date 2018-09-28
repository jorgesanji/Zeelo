package com.test.books.ui.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.File

object ImageLoader {

    fun loadBackground(imageView: ImageView, url:String?){
        loadImage(imageView, url, null, false, false)
    }

    fun loadImageFromPath(imageView: ImageView, path:String?){
        val file = File(path)
        loadImage(imageView, file, null, false, false)
    }

    private fun loadImage(imageView: ImageView, image: Any?, placeHolder:Int?, roundCorners:Boolean = true, resize:Boolean = true) {
        var creator:RequestCreator? = null
        if (image is File){
            creator = Picasso.with(imageView.context).load(image)
        }else{
            creator = Picasso.with(imageView.context).load(image as String)
        }
        if (placeHolder != null){
            creator.placeholder(placeHolder)
        }
        if (roundCorners){
            creator.transform(RoundCornersTransform(false))
        }
        if(resize){
            creator.fit()
            creator.centerCrop()
        }
        creator.into(imageView)
    }
}
