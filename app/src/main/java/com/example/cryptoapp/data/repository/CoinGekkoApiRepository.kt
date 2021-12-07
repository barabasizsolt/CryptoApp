package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.source.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.model.event.AllEvents
import com.example.cryptoapp.data.model.exchange.Exchange
import retrofit2.Response

class CoinGekkoApiRepository {
    suspend fun getAllExchanges(perPage : Int, page : String) : Response<List<Exchange>> {
        return CoinGekkoRetrofitInstance.CoinGekkoApi.getExchanges(perPage = perPage, page = page)
    }

    suspend fun getAllEvents(page : String) : Response<AllEvents> {
        return CoinGekkoRetrofitInstance.CoinGekkoApi.getEvents(page = page)
    }
}