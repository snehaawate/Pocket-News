package com.example.pocketnews.repository

import app.cash.turbine.test
import com.example.pocketnews.data.api.NetworkService
import com.example.pocketnews.data.local.DatabaseService
import com.example.pocketnews.data.local.entity.Article
import com.example.pocketnews.data.local.entity.Source
import com.example.pocketnews.data.model.topheadlines.ApiArticle
import com.example.pocketnews.data.model.topheadlines.ApiSource
import com.example.pocketnews.data.model.topheadlines.TopHeadlinesResponse
import com.example.pocketnews.data.repository.OfflineTopHeadlineRepository
import com.example.pocketnews.utils.AppConstant
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class OfflineTopHeadlineRepositoryTest {

    @Mock
    private lateinit var networkService: NetworkService

    @Mock
    private lateinit var databaseService: DatabaseService

    private lateinit var offlineTopHeadlineRepository: OfflineTopHeadlineRepository

    @Before
    fun setUp() {
        offlineTopHeadlineRepository = OfflineTopHeadlineRepository(networkService, databaseService)
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val country = AppConstant.COUNTRY
            val apiSource = ApiSource(id = "SourceId", name = "sourceName")
            val apiArticle = ApiArticle(
                title = "title",
                description = "description",
                url = "url",
                imageUrl = "urlToImage",
                apiSource = apiSource
            )

            val listOfArticleAPI = mutableListOf<ApiArticle>()
            listOfArticleAPI.add(apiArticle)

            val topHeadlinesResponse = TopHeadlinesResponse(
                status = "ok", totalResults = 1, apiArticles = listOfArticleAPI
            )

            doReturn(topHeadlinesResponse)
                .`when`(networkService).getTopHeadlines(country)

            offlineTopHeadlineRepository.getTopHeadlinesArticles(AppConstant.COUNTRY).test {
                assertEquals(topHeadlinesResponse.apiArticles, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(networkService, times(1)).getTopHeadlines(country)
        }
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseError_shouldReturnError() {
        runTest {
            val country = AppConstant.COUNTRY
            val errorMessage = "Enter Message For You"

            doThrow(RuntimeException(errorMessage)).`when`(networkService)
                .getTopHeadlines(country)

            offlineTopHeadlineRepository.getTopHeadlinesArticles(country).test {
                assertEquals(errorMessage, awaitError().message)
                cancelAndIgnoreRemainingEvents()
            }
            verify(networkService, times(1)).getTopHeadlines(country)
        }
    }

    @Test
    fun getTopHeadlinesFromDB_whenDatabaseServiceResponseSuccess_shouldReturnSuccess() {
        runTest {
            val country = AppConstant.COUNTRY
            val source = Source(
                id = "SourceId", name = "sourceName"
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

            doReturn(flowOf(listOfArticle)).`when`(databaseService)
                .getAllTopHeadlinesArticles(country)

            offlineTopHeadlineRepository.getTopHeadlinesArticlesFromDB(country).test {
                assertEquals(listOfArticle, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            verify(databaseService, times(1)).getAllTopHeadlinesArticles(country)
        }
    }
}