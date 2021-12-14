package com.example.cryptoapp.feature.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.ExchangeConstant
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.repository.ExchangeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ExchangeViewModel(private val repository: ExchangeRepository) : ViewModel() {
    val exchanges: MutableStateFlow<Response<List<Exchange>>?> = MutableStateFlow(null)

    fun loadExchanges(perPage: Int = ExchangeConstant.PER_PAGE, page: String = ExchangeConstant.PAGE) {
        viewModelScope.launch {
            val response = repository.getAllExchanges(perPage = perPage, page = page)
            exchanges.value = response
        }
    }
}
