package com.example.cryptoapp.feature.exchange

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.ExchangeConstant
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.repository.CoinGekkoApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ExchangeViewModel(private val repository: CoinGekkoApiRepository) : ViewModel() {
    private val exchanges: MutableLiveData<Response<List<Exchange>>> = MutableLiveData()

    fun loadExchanges(perPage: Int = ExchangeConstant.PER_PAGE, page: String = ExchangeConstant.PAGE) {
        viewModelScope.launch {
            val response = repository.getAllExchanges(perPage = perPage, page = page)
            exchanges.value = response
        }
    }

    fun getExchanges() = exchanges
}
