package com.example.cryptoapp.model.events

data class AllEvents (
    val data : List<Event>,
    val count: Long = 0,
    val page: Long = 0
    )