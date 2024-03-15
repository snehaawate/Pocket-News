package com.example.pocketnews.ui.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.local.entity.Article
import com.example.pocketnews.data.model.topheadlines.toArticleEntity
import com.example.pocketnews.data.repository.OfflineTopHeadlineRepository
import com.example.pocketnews.ui.base.UiState
import com.example.pocketnews.utils.AppConstant
import com.example.pocketnews.utils.DispatcherProvider
import com.example.pocketnews.utils.NetworkHelper
import com.example.pocketnews.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineTopHeadlineViewModel @Inject constructor(
    private val topHeadlineRepository: OfflineTopHeadlineRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: Logger
) : ViewModel() {

    private val _topHeadlineUiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val topHeadlineUiState: StateFlow<UiState<List<Article>>> = _topHeadlineUiState

    private fun checkInternetConnection(): Boolean = networkHelper.isNetworkConnected()

    init {
        startFetchingArticles()
    }

    fun startFetchingArticles() {
        if (checkInternetConnection()) {
            fetchArticles()
        } else {
            fetchArticlesFromDB()
        }
    }

    private fun fetchArticles() {
        viewModelScope.launch(dispatcherProvider.main) {
            topHeadlineRepository.getTopHeadlinesArticles(AppConstant.COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _topHeadlineUiState.value = UiState.Error(e.toString())
                }.map {
                    it.map { apiArticle -> apiArticle.toArticleEntity(AppConstant.COUNTRY) }
                }.flatMapConcat {
                    flow {
                        emit(
                            topHeadlineRepository.deleteAndInsertAllTopHeadlinesArticles(
                                it,
                                AppConstant.COUNTRY
                            )
                        )
                    }
                }.flowOn(dispatcherProvider.io).catch { e ->
                    _topHeadlineUiState.value = UiState.Error(e.toString())
                }.collect {
                    fetchArticlesFromDB()
                }
        }
    }

    private fun fetchArticlesFromDB() {
        viewModelScope.launch(dispatcherProvider.main) {
            topHeadlineRepository.getTopHeadlinesArticlesFromDB(AppConstant.COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _topHeadlineUiState.value = UiState.Error(e.toString())
                }
                .collect {
                    if (!checkInternetConnection() && it.isEmpty()) {
                        _topHeadlineUiState.value = UiState.Error("Data Not found.")
                    } else {
                        _topHeadlineUiState.value = UiState.Success(it)
                        logger.d("OfflineTopHeadlineViewModel", "Success")
                    }
                }
        }
    }
}