package com.test.books.di.module

import android.content.Context
import com.test.books.business.usecase.books.*
import com.test.books.data.repository.RepositoryProxy
import com.test.books.di.PerFragment
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule(private val activity: Context) {

    @Provides
    @PerFragment
    internal fun provideGetBooks(repositoryFactory: RepositoryProxy): GetBooksUseCase {
        return GetBooks(repositoryFactory)
    }

    @Provides
    @PerFragment
    internal fun provideGetBookDetail(repositoryFactory: RepositoryProxy): GetBookDetailUseCase {
        return GetBookDetail(repositoryFactory)
    }

    @Provides
    @PerFragment
    internal fun provideCreateeBook(repositoryFactory: RepositoryProxy): CreateBookUseCase{
        return CreateBook(repositoryFactory)
    }
}
