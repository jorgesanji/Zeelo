package com.test.books.ui.view.presenter.home

import android.app.Activity
import com.test.books.business.GetBooksTestUseCase
import com.test.books.ui.presenter.home.HomePresenter
import com.test.books.ui.view.IONavigation
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(HomePresenter::class)
class HomePresenterTest {

    lateinit private var homePresenter: HomePresenter
    @Mock
    lateinit private var mockAppNavigation: IONavigation
    @Mock
    lateinit private var mockGetMovies: GetBooksTestUseCase
    @Mock
    lateinit private var mockView: HomePresenter.View
    @Mock
    lateinit private var mockActivity: Activity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this);
        given(mockView.activity).willReturn(mockActivity)
        homePresenter = HomePresenter(mockAppNavigation, mockGetMovies)
        homePresenter.attachView(mockView)
    }

    @After
    fun validate() {
        validateMockitoUsage()
    }

    @Test
    fun testPresenterGetMovies() {
        assert(homePresenter.count == 0){"Error size movie array"}
        homePresenter.getBooks()
        verify(mockView, atLeastOnce()).showLoading()
    }

    @Test
    fun testGetFirstPage() {
        mockGetMovies.offset = 0
        mockGetMovies.count = 10
        mockGetMovies.subscribe(null)
        verify(mockGetMovies, atLeastOnce()).subscribe(null)
    }
}