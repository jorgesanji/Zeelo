package com.test.books.data.rest.service

import com.test.books.data.rest.service.RestConstants.accessTokenKeyName
import com.test.books.data.rest.service.RestConstants.connect_time_out
import com.test.books.data.rest.service.RestConstants.platformKeyName
import com.test.books.data.rest.service.RestConstants.platform_type
import com.test.books.data.rest.service.RestConstants.read_time_out
import com.test.books.data.utils.RetrofitUtils
import com.test.books.data.utils.RxErrorHandlingCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.HttpURLConnection.HTTP_OK
import java.util.concurrent.TimeUnit

interface OAuthListener{
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun updateSession(validation: SessionUtils.Validation)
    fun onRefreshSessionFailed()
}

class RestAdapter {

    var service: RestApiService
    var accessToken: String? = null
    var sessionHandler:OAuthListener? = null

    init {
        this.service = generateApiService(true)
    }

    private fun generateApiService(auth:Boolean = true): RestApiService {
        val okHttpClient = generateOkHttpClient(auth).build()
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .baseUrl(RestConstants.BaseUrl)
                .addConverterFactory(RetrofitUtils.buildGSONConverter())
                .client(okHttpClient)
                .build()
        return retrofit.create(RestApiService::class.java!!)
    }

    private fun generateOkHttpClient(auth:Boolean = true): OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(connect_time_out, TimeUnit.SECONDS)
        httpClient.readTimeout(read_time_out, TimeUnit.SECONDS)
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpInterceptor())
        if (auth){
            httpClient.authenticator(TokenAuthenticator())
        }
        httpClient.retryOnConnectionFailure(true)
        return httpClient
    }

    private fun buildRequest(original: Request?): Request {
        val builder = original!!.newBuilder()
                .header(platformKeyName, platform_type)
                .method(original!!.method(), original!!.body())
        if (accessToken != null) {
            builder.header(accessTokenKeyName, accessToken)
        }
        return builder.build()
    }

    private fun logout(){
        service = generateApiService(false)
        sessionHandler!!.onRefreshSessionFailed()
    }

    private inner class TokenAuthenticator : Authenticator {

        override fun authenticate(route: Route, response: Response): Request {
            try {
                val refreshToken = sessionHandler!!.getRefreshToken()
                val oAuthCredentials = SessionUtils.updateCredentials(refreshToken!!)
                if (oAuthCredentials.validation != null && oAuthCredentials.code === HTTP_OK) {
                    val validation = oAuthCredentials.validation
                    this@RestAdapter.accessToken = validation.token.accesToken
                    sessionHandler!!.updateSession(validation)
                } else if (oAuthCredentials.apiError != null || oAuthCredentials.apiError!!.isRefreshTokenExpired) {
                    logout()
                }
            } catch (e: Exception) {
                //Timber.d("RESTADAPTER_ERROR_UPDATING_ACCESS_TOKEN =" + e.message)
            }
            return buildRequest(response.request())
        }
    }

    private inner class HttpInterceptor() : Interceptor {

        override fun intercept(chain: Interceptor.Chain?): Response {
            return chain!!.proceed(buildRequest(chain!!.request()))
        }
    }
}