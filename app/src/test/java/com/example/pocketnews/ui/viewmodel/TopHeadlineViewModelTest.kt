package com.example.pocketnews.ui.viewmodel

import TestLogger
import app.cash.turbine.test
import com.example.pocketnews.data.local.entity.Article
import com.example.pocketnews.data.repository.TopHeadlineRepository
import com.example.pocketnews.ui.base.UiState
import com.example.pocketnews.ui.topheadline.TopHeadlineViewModel
import com.example.pocketnews.utils.AppConstant
import com.example.pocketnews.utils.DispatcherProvider
import com.example.pocketnews.utils.NetworkHelper
import com.example.pocketnews.utils.TestDispatcherProvider
import com.example.pocketnews.utils.TestNetworkHelper
import com.example.pocketnews.utils.logger.Logger
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TopHeadlineViewModelTest {

    @Mock
    private lateinit var topHeadlinesRepository: TopHeadlineRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var networkHelper: NetworkHelper

    private lateinit var logger: Logger

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        logger = TestLogger()
    }

    @Test
    fun fetchNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val country = AppConstant.COUNTRY
            doReturn(flowOf(emptyList<Article>()))
                .`when`(topHeadlinesRepository)
                .getTopHeadlinesArticles(country)
            val viewModel = TopHeadlineViewModel(
                topHeadlinesRepository,
                dispatcherProvider,
                networkHelper,
                logger
            )
            viewModel.topHeadlineUiState.test {
                assertEquals(UiState.Success(emptyList<List<Article>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(topHeadlinesRepository, times(1)).getTopHeadlinesArticles(country)
        }
    }

    @Test
    fun fetchNews_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val country = AppConstant.COUNTRY
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(topHeadlinesRepository)
                .getTopHeadlinesArticles(country)

            val viewModel = TopHeadlineViewModel(
                topHeadlinesRepository,
                dispatcherProvider,
                networkHelper,
                logger
            )
            viewModel.topHeadlineUiState.test {
                assertEquals(
                    UiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(topHeadlinesRepository, times(1)).getTopHeadlinesArticles(country)
        }
    }

}