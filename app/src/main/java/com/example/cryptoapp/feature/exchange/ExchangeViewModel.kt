package com.example.cryptoapp.feature.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.ExchangeConstant
import com.example.cryptoapp.data.constant.ExchangeConstant.toExchangeUIModel
import com.example.cryptoapp.data.model.exchange.ExchangeUIModel
import com.example.cryptoapp.domain.exchange.GetExchangesUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExchangeViewModel(private val useCase: GetExchangesUseCase) : ViewModel() {
    val exchanges = MutableStateFlow<List<ExchangeUIModel>?>(listOf())

    fun loadExchanges(perPage: Int = ExchangeConstant.PER_PAGE, page: String = ExchangeConstant.PAGE) {
        viewModelScope.launch {
            when (val result = useCase(perPage = perPage, page = page)) {
                is Result.Success -> {
                    exchanges.value = result.data.map { exchange ->
                        exchange.toExchangeUIModel()
                    }
                }
                is Result.Failure -> {
                    exchanges.value = listOf()
                }
            }
        }
    }

    init {
        loadExchanges()
    }
}
