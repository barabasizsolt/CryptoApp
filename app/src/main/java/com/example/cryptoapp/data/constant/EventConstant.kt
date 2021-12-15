package com.example.cryptoapp.data.constant

import com.example.cryptoapp.data.model.event.Event
import com.example.cryptoapp.data.model.event.EventResponse
import com.example.cryptoapp.data.model.event.EventUIModel

object EventConstant {
    const val PAGE = "1"

    fun EventResponse.toEvent() = Event(
        event = this
    )

    fun Event.toEventUIModel() = EventUIModel(
        title = event.title.toString(),
        organizer = event.organizer.toString(),
        startDate = event.startDate.toString(),
        endDate = event.endDate.toString(),
        logo = event.screenshot.toString()
    )
}
