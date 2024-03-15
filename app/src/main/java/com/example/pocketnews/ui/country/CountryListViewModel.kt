package com.example.pocketnews.ui.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketnews.data.model.Country
import com.example.pocketnews.data.repository.CountryListRepository
import com.example.pocketnews.ui.base.UiState
import com.example.pocketnews.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(
    private val countryListRepository: CountryListRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _countryUiState = MutableStateFlow<UiState<List<Country>>>(UiState.Loading)

    val countryUiState: StateFlow<UiState<List<Country>>> = _countryUiState

    init {
        fetchCountry()
    }

    fun fetchCountry() {
        viewModelScope.launch(dispatcherProvider.main) {
            countryListRepository.getCountry()
                .flowOn(dispatcherProvider.default)
                .catch { e ->
                    _countryUiState.value = UiState.Error(e.toString())
                }.collect {
                    _countryUiState.value = UiState.Success(it)
                }
        }
    }
}
