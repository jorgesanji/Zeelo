package com.test.books.ui.view.home

import android.app.Application
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.test.books.data.model.Book
import com.test.books.data.repository.RestRepository
import com.test.books.di.component.ApplicationComponent
import com.test.books.di.module.ApplicationModule
import com.test.books.ui.application.SampleApplication
import com.test.books.ui.presenter.home.HomePresenter
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
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeViewTest {

    @Rule
    var daggerRule: DaggerMockRule<ApplicationComponent> = DaggerMockRule(
            ApplicationComponent::class.java,
            ApplicationModule(getInstrumentation().getTargetContext().getApplicationContext() as Application))
            .set({ component ->
                val app = getInstrumentation().getTargetContext().getApplicationContext() as SampleApplication
                app.applicationComponent = component
            })

    @Mock
    lateinit var homePresenter: HomePresenter

    @Mock
    lateinit var repository: RestRepository

    lateinit var homeFragment: HomeFragment

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        homeFragment = HomeFragment()
        homeFragment.presenter = homePresenter
    }

    @Test
    @Throws(Exception::class)
    fun testAttachView() {
        verify(homePresenter).attachView(homeFragment)
    }

    @Test
    @Throws(Exception::class)
    fun testGetBookItem() {
        assert(homePresenter.getItemAtPosition(0) != null){"Error retrieving one element of the list"}
        verify(homePresenter).getItemAtPosition(0)
    }

    @Test
    @Throws(Exception::class)
    fun testGetBooks() {
        val array = ArrayList<Book>()
        val response = array.toTypedArray()
        `when`(repository.getBookList(0, 10))
                .thenReturn(Observable.just<Array<Book>>(response))
    }
}