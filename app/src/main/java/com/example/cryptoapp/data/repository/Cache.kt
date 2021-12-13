package com.example.cryptoapp.data.repository

object Cache {
    private val userWatchLists: MutableList<String> = mutableListOf()

    fun addUserWatchList(data: String) {
        userWatchLists.add(data)
    }

    fun getUserWatchList() = userWatchLists

    fun deleteUserWatchList() = userWatchLists.clear()
}
