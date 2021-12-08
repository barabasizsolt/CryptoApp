package com.example.cryptoapp.feature.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.ExchangeConstant.PAGE
import com.example.cryptoapp.data.constant.ExchangeConstant.PER_PAGE
import com.example.cryptoapp.data.model.event.AllEvents
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.repository.CoinGekkoApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CoinGekkoApiViewModel(private val repository: CoinGekkoApiRepository) : ViewModel() {
    val allExchangeResponse: MutableLiveData<Response<List<Exchange>>> = MutableLiveData()
    val allEventsResponse: MutableLiveData<Response<AllEvents>> = MutableLiveData()

    fun getAllExchanges(perPage: Int = PER_PAGE, page: String = PAGE) {
        viewModelScope.launch {
            val response = repository.getAllExchanges(perPage = perPage, page = page)
            allExchangeResponse.value = response
        }
    }

    fun getAllEvents(page: String) {
        viewModelScope.launch {
            val response = repository.getAllEvents(page = page)
            allEventsResponse.value = response
        }
    }
}
