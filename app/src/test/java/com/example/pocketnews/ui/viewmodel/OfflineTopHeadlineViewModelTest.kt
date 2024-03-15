package com.example.pocketnews.ui.viewmodel

import TestLogger
import app.cash.turbine.test
import com.example.pocketnews.data.local.entity.Article
import com.example.pocketnews.data.local.entity.Source
import com.example.pocketnews.data.model.topheadlines.ApiArticle
import com.example.pocketnews.data.model.topheadlines.ApiSource
import com.example.pocketnews.data.model.topheadlines.toArticleEntity
import com.example.pocketnews.data.repository.OfflineTopHeadlineRepository
import com.example.pocketnews.ui.base.UiState
import com.example.pocketnews.ui.offline.OfflineTopHeadlineViewModel
import com.example.pocketnews.utils.AppConstant
import com.example.pocketnews.utils.DispatcherProvider
import com.example.pocketnews.utils.NetworkHelperImpl
import com.example.pocketnews.utils.TestDispatcherProvider
import com.example.pocketnews.utils.logger.Logger
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class OfflineTopHeadlineViewModelTest {

    @Mock
    private lateinit var offlineTopHeadlinesRepository: OfflineTopHeadlineRepository

    @Mock
    private lateinit var networkConnection: NetworkHelperImpl

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var logger: Logger

    private lateinit var offlineTopHeadlineViewModel: OfflineTopHeadlineViewModel

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        logger = TestLogger()
    }

    @Test
    fun fetchNews_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val country = AppConstant.COUNTRY
            val apiSource = ApiSource(
                id = "sourceId", name = "sourceName"
            )
            val apiArticle = ApiArticle(
                title = "title",
                description = "description",
                url = "url",
                imageUrl = "urlToImage",
                apiSource = apiSource
            )

            val listOfArticleAPI = mutableListOf<ApiArticle>()
            listOfArticleAPI.add(apiArticle)

            val article = apiArticle.toArticleEntity(country)
            val listOfArticle = mutableListOf<Article>()
            listOfArticle.add(article)

            doReturn(true).`when`(networkConnection).isNetworkConnected()

            doReturn(flowOf(listOfArticleAPI)).`when`(offlineTopHeadlinesRepository)
                .getTopHeadlinesArticles(country)
            doNothing().`when`(offlineTopHeadlinesRepository)
                .deleteAndInsertAllTopHeadlinesArticles(listOfArticle, country)

            offlineTopHeadlineViewModel = OfflineTopHeadlineViewModel(offlineTopHeadlinesRepository, dispatcherProvider, networkConnection, logger)
            verify(offlineTopHeadlinesRepository, times(1)).getTopHeadlinesArticles(country)
            verify(offlineTopHeadlinesRepository, times(1)).deleteAndInsertAllTopHeadlinesArticles(listOfArticle, country)
        }
    }

    @Test
    fun fetchNews_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            doReturn(true).`when`(networkConnection).isNetworkConnected()
            val country = AppConstant.COUNTRY
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(offlineTopHeadlinesRepository).getTopHeadlinesArticles(country)
            offlineTopHeadlineViewModel = OfflineTopHeadlineViewModel(offlineTopHeadlinesRepository, dispatcherProvider, networkConnection, logger)
            offlineTopHeadlineViewModel.topHeadlineUiState.test {
                assertEquals(UiState.Error(IllegalStateException(errorMessage).toString()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(offlineTopHeadlinesRepository, times(1)).getTopHeadlinesArticles(country)
        }
    }

    @Test
    fun fetchNewsNoInternet_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            val country = AppConstant.COUNTRY
            val source = Source(
                id = "sourceId", name = "sourceName"
            )
            val article = Article(
                title = "title",
                description = "description",
                url = "url",
                imageUrl = "urlToImage",
                source = source
            )

            val listOfArticle = mutableListOf<Article>()
            listOfArticle.add(article)

            doReturn(false).`when`(networkConnection).isNetworkConnected()

            doReturn(flowOf(listOfArticle)).`when`(offlineTopHeadlinesRepository)
                .getTopHeadlinesArticlesFromDB(country)

            offlineTopHeadlineViewModel = OfflineTopHeadlineViewModel(offlineTopHeadlinesRepository, dispatcherProvider, networkConnection, logger)

            offlineTopHeadlineViewModel.topHeadlineUiState.test {
                assertEquals(UiState.Success(listOfArticle), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(offlineTopHeadlinesRepository, times(1)).getTopHeadlinesArticlesFromDB(country)
        }
    }

    @Test
    fun fetchNewsNoInternet_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val country = AppConstant.COUNTRY
            doReturn(false).`when`(networkConnection).isNetworkConnected()
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Article>> {
                throw IllegalStateException(errorMessage)
            })
                .`when`(offlineTopHeadlinesRepository).getTopHeadlinesArticlesFromDB(country)
            offlineTopHeadlineViewModel = OfflineTopHeadlineViewModel(offlineTopHeadlinesRepository, dispatcherProvider, networkConnection, logger)
            offlineTopHeadlineViewModel.topHeadlineUiState.test {
                assertEquals(
                    UiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(offlineTopHeadlinesRepository, times(1)).getTopHeadlinesArticlesFromDB(country)
        }
    }
}