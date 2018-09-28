package com.test.books.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Book() : Parcelable{

    @SerializedName("id")
    var id:Int? = null

    @SerializedName("title")
    var title:String? = null

    @SerializedName("author")
    var author:String? = null

    @SerializedName("price")
    var price: Double? = null

    @SerializedName("image")
    var image:String? = null

    @SerializedName("link")
    var link:String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        author = parcel.readString()
        price = parcel.readDouble()
        image = parcel.readString()
        link = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeInt(id!!)
            dest.writeString(title)
            dest.writeString(author)
            dest.writeDouble(price!!)
            dest.writeString(image)
            dest.writeString(link)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}