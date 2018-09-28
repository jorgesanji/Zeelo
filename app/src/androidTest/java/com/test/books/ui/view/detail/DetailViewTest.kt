package com.test.books.ui.view.detail

import android.app.Application
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.test.books.data.model.Book
import com.test.books.data.repository.RestRepository
import com.test.books.di.component.ApplicationComponent
import com.test.books.di.module.ApplicationModule
import com.test.books.ui.application.SampleApplication
import com.test.books.ui.presenter.detail.DetailPresenter
import it.cosenonjaviste.daggermock.DaggerMockRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import rx.Observable

@RunWith(AndroidJUnit4::class)
@LargeTest
class DetailViewTest {

    @Rule
    var daggerRule: DaggerMockRule<ApplicationComponent> = DaggerMockRule(
            ApplicationComponent::class.java,
            ApplicationModule(getInstrumentation().getTargetContext().getApplicationContext() as Application))
            .set({ component ->
                val app = getInstrumentation().getTargetContext().getApplicationContext() as SampleApplication
                app.applicationComponent = component
            })

    @Mock
    lateinit var homePresenter: DetailPresenter

    @Mock
    lateinit var repository: RestRepository

    lateinit var detailFragment: DetailFragment

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        detailFragment = DetailFragment()
        detailFragment.presenter = homePresenter
    }

    @Test
    @Throws(Exception::class)
    fun testAttachView() {
        verify(homePresenter).attachView(detailFragment)
    }

    @Test
    @Throws(Exception::class)
    fun testGetBook() {
        verify(homePresenter).getBookDetail()
    }

    @Test
    @Throws(Exception::class)
    fun testGetBookDetail() {
        val book = Book()
        `when`(repository.getBookDetail(8))
                .thenReturn(Observable.just<Book>(book))
    }
}