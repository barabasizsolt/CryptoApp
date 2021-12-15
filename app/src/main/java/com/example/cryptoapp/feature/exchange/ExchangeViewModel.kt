package com.example.cryptoapp.feature.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.ExchangeConstant
import com.example.cryptoapp.data.constant.ExchangeConstant.DEFAULT_PAGE
import com.example.cryptoapp.data.constant.ExchangeConstant.toExchangeUIModel
import com.example.cryptoapp.data.model.exchange.ExchangeUIModel
import com.example.cryptoapp.domain.exchange.GetExchangesUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExchangeViewModel(private val useCase: GetExchangesUseCase) : ViewModel() {
    private val _exchanges = MutableStateFlow(emptyList<ExchangeUIModel>())
    val exchanges: Flow<List<ExchangeUIModel>> = _exchanges

    fun loadExchanges(perPage: Int = ExchangeConstant.PER_PAGE, page: String = DEFAULT_PAGE) {
        viewModelScope.launch {
            when (val result = useCase(perPage = perPage, page = page)) {
                is Result.Success -> {
                    val exchangeResults = result.data.map { exchange ->
                        exchange.toExchangeUIModel()
                    } as MutableList
                    _exchanges.value = ((_exchanges.value + exchangeResults) as MutableList<ExchangeUIModel>)
                }
                is Result.Failure -> {
                    _exchanges.value = mutableListOf()
                }
            }
        }
    }

    init {
        loadExchanges()
    }
}
