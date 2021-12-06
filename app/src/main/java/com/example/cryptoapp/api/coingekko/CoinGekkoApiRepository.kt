package com.example.cryptoapp.api.coingekko

import com.example.cryptoapp.model.events.AllEvents
import com.example.cryptoapp.model.exchanges.Exchange
import retrofit2.Response

class CoinGekkoApiRepository {
    suspend fun getAllExchanges(perPage : Int, page : String) : Response<List<Exchange>> {
        return CoinGekkoRetrofitInstance.CoinGekkoApi.getExchanges(perPage = perPage, page = page)
    }

    suspend fun getAllEvents(page : String) : Response<AllEvents> {
        return CoinGekkoRetrofitInstance.CoinGekkoApi.getEvents(page = page)
    }
}