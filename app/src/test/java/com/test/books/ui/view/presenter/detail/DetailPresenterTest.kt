package com.test.books.ui.view.presenter.detail

import android.app.Activity
import com.test.books.business.GetBookDetailTest
import com.test.books.ui.presenter.detail.DetailPresenter
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
class DetailPresenterTest {

    lateinit private var detailPresenter: DetailPresenter
    @Mock
    lateinit private var mockAppNavigation: IONavigation
    @Mock
    lateinit private var mockGetBookDetail: GetBookDetailTest
    @Mock
    lateinit private var mockView: DetailPresenter.View
    @Mock
    lateinit private var mockActivity: Activity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this);
        given(mockView.activity).willReturn(mockActivity)
        detailPresenter = DetailPresenter(mockAppNavigation, mockGetBookDetail)
        detailPresenter.attachView(mockView)
    }

    @After
    fun validate() {
        validateMockitoUsage()
    }

    @Test
    fun testPresenterGetMovies() {
        detailPresenter.getBookDetail()
        verify(mockView, atLeastOnce()).showLoading()
    }

    @Test
    fun testGetFirstPage() {
        mockGetBookDetail.id = 8
        mockGetBookDetail.subscribe(null)
        verify(mockGetBookDetail, atLeastOnce()).subscribe(null)
    }
}