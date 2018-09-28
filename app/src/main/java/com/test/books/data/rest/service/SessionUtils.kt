package com.test.books.data.rest.service

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.test.books.data.rest.service.RestConstants.HTTP_OK
import com.test.books.data.rest.service.RestConstants.milliseconds
import com.test.books.data.rest.service.RestConstants.connect_time_out
import com.test.books.data.rest.service.RestConstants.platformKeyName
import com.test.books.data.rest.service.RestConstants.platform_type
import com.test.books.data.rest.service.RestConstants.read_time_out
import com.test.books.data.rest.service.RestConstants.refreshTokenKeyName
import com.test.books.data.utils.RetrofitUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

object SessionUtils{

    private val REFRESH_URL = RestConstants.BaseUrl + "token/refresh"

    @Synchronized
    @Throws(Exception::class)
    fun updateCredentials(refreshToken: String): OAuthCredentials {
        val httpsUrlConnection = getRefreshTokenConnection(REFRESH_URL, refreshToken, true) as HttpURLConnection
        //Timber.d("REFRESH_TOKEN_HEADERS = " + httpsUrlConnection.requestProperties.toString())
        httpsUrlConnection.connect()
        val responseCode = httpsUrlConnection.responseCode
        val gson = RetrofitUtils.gson
        if (responseCode == HTTP_OK) {
            val responseString = getStringFromImputStream(httpsUrlConnection.inputStream)
            //Timber.d("REFRESH_TOKEN_RESPONSE = " + responseString + " --> LAST_UPDATE = " + Date().toLocaleString())
            val validation = gson.fromJson<Validation>(responseString, Validation::class.java!!)
            return OAuthCredentials(validation, null, responseCode)
        }
        val errorString = getStringFromImputStream(httpsUrlConnection.errorStream)
        //Timber.d("REFRESH_TOKEN_RESPONSE_ERROR = $errorString")
        val apiError = gson.fromJson<ApiError>(errorString, ApiError::class.java!!)
        return OAuthCredentials(null, apiError, responseCode)
    }

    @Throws(Exception::class)
    private fun getRefreshTokenConnection(url: String, refreshToken: String,
                                          secure: Boolean): URLConnection {
        val refreshUrl = URL(url)
        val urlConnection = refreshUrl.openConnection()
        if (secure && urlConnection is HttpsURLConnection) {
            val sslSocketFactory = RetrofitUtils.createTrustAllSslSocketFactory()
            urlConnection.sslSocketFactory = sslSocketFactory
        }
        (urlConnection as HttpURLConnection).requestMethod = "GET"
        urlConnection.setRequestProperty("Content-Type", "application/json")
        urlConnection.setRequestProperty("Accept", "application/json")
        urlConnection.setRequestProperty(refreshTokenKeyName, refreshToken)
        urlConnection.setRequestProperty(platformKeyName, platform_type)
        urlConnection.setUseCaches(false)
        urlConnection.setAllowUserInteraction(false)
        urlConnection.setConnectTimeout((connect_time_out * milliseconds) as Int)
        urlConnection.setReadTimeout((read_time_out * milliseconds) as Int)
        return urlConnection
    }

    @Throws(Exception::class)
    private fun getStringFromImputStream(imputStream: InputStream?): String {
        if (imputStream == null) {
            return ""
        }
        val bufferedReader = BufferedReader(InputStreamReader(imputStream))
        val stringBuilder = StringBuilder()
        var line : String?
        do {
            line = bufferedReader.readLine()
            if (line == null)
                break
            stringBuilder.append(line)
        } while (true)
        bufferedReader.close()
        return stringBuilder.toString()
    }

    class  OAuthCredentials(val validation: Validation?, val apiError: ApiError?, val code: Int)

    class Validation: Parcelable {

        @SerializedName("token")
        var token: Token

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeParcelable(this.token, flags)
        }

        constructor(parcel: Parcel){
            this.token = parcel.readParcelable(Token::class.java.classLoader)
        }

        companion object CREATOR : Parcelable.Creator<Validation> {
            override fun createFromParcel(parcel: Parcel): Validation {
                return Validation(parcel)
            }

            override fun newArray(size: Int): Array<Validation?> {
                return arrayOfNulls(size)
            }
        }
    }

    class Token : Parcelable {

        @SerializedName("accessToken")
        var accesToken: String

        @SerializedName("refreshToken")
        var refreshToken: String

        @SerializedName("expiresIn")
        var time: Int = 0

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(this.accesToken)
            dest.writeString(this.refreshToken)
            dest.writeInt(this.time)
        }

        protected constructor(`in`: Parcel) {
            this.accesToken = `in`.readString()
            this.refreshToken = `in`.readString()
            this.time = `in`.readInt()
        }

        companion object CREATOR : Parcelable.Creator<Token> {
            override fun createFromParcel(parcel: Parcel): Token {
                return Token(parcel)
            }

            override fun newArray(size: Int): Array<Token?> {
                return arrayOfNulls(size)
            }
        }
    }

}

