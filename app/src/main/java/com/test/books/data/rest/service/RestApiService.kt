package com.test.books.data.rest.service

import com.test.books.data.model.Book
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

interface RestApiService {

    @GET("items")
    fun getBookList(@Query("offset")page:Int, @Query("count")count:Int) : Observable<Array<Book>>

    @GET("items/{id}")
    fun getBookDetail(@Path("id")id:Int) : Observable<Book>

    @POST("item")
    fun postBook(@Body book: Book) : Observable<Book>

    @Multipart
    @POST("item")
    fun postBook(@Part("file\"; filename=\"pp.png\" ") file: RequestBody?, @Part("title") title: RequestBody, @Part("author") author: RequestBody, @Part("price") price: RequestBody): Observable<Book>
}
