package com.test.books.integration

import android.app.Application

import com.test.books.data.repository.LocalRepository
import com.test.books.data.repository.MockRepository
import com.test.books.data.repository.RepositoryProxy
import com.test.books.data.repository.RestRepository

class  RepositoryProxyTest(context: Application, restRepository: RestRepository,
                          localRepository: LocalRepository, mockRepository: MockRepository) : RepositoryProxy(context, restRepository, localRepository, mockRepository) {

    override val isNetworkAvailable: Boolean
        get() = true
}
