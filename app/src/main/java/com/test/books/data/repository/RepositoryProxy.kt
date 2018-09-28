package com.test.books.data.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

open class RepositoryProxy @Inject
constructor(private val context: Application, private val restRepository: RestRepository, private val localRepository: LocalRepository, private val mockRepository: MockRepository) {

    val repository: Repository
        get() = if (isNetworkAvailable) restRepository else localRepository

    protected open val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    enum class RepositoryType {
        MOCK,
        REST,
        LOCAL
    }

    fun getRepository(repositoryType: RepositoryType): Repository {
        when (repositoryType) {
            RepositoryProxy.RepositoryType.LOCAL -> return localRepository
            RepositoryProxy.RepositoryType.MOCK -> return mockRepository
            else -> return restRepository
        }
    }
}