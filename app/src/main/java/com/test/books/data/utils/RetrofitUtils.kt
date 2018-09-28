package com.test.books.data.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.books.ui.utils.DateTypeDeserializer
import com.test.books.ui.utils.DateTypeSerializer
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitUtils {

    val gson: Gson
        get() = builder.create()

    val builder: GsonBuilder
        get() {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Date::class.java, DateTypeSerializer())
            builder.registerTypeAdapter(Date::class.java, DateTypeDeserializer())
            return builder
        }

    fun buildGSONConverter(): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    fun addSSLToOkClient(httpClient: OkHttpClient.Builder): OkHttpClient.Builder {
        try {
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = createTrustAllSslSocketFactory()
            httpClient.sslSocketFactory(sslSocketFactory)
            httpClient.hostnameVerifier { hostname, session -> true }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return httpClient
    }

    @Throws(Exception::class)
    fun createTrustAllSslSocketFactory(): SSLSocketFactory {
        val byPassTrustManagers = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }

            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, byPassTrustManagers, SecureRandom())
        return sslContext.socketFactory
    }

    @Throws(NoSuchAlgorithmException::class)
    fun md5(s: String): String {
        // Create MD5 Hash
        val digest = MessageDigest
                .getInstance("MD5")
        digest.update(s.toByteArray())
        val messageDigest = digest.digest()
        val bigInt = BigInteger(1, messageDigest)
        var hashText = bigInt.toString(16)
        // Now we need to zero pad it if you actually want the full 32
        // chars.
        while (hashText.length < 32) {
            hashText = "0$hashText"
        }
        return hashText
    }
}