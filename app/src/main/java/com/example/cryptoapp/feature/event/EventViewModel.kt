package com.example.cryptoapp.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.EventConstant.DEFAULT_PAGE
import com.example.cryptoapp.data.model.event.Event
import com.example.cryptoapp.domain.event.GetEventsUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val useCase: GetEventsUseCase) : ViewModel() {
    private val _events = MutableStateFlow(emptyList<EventUIModel>())
    val events: Flow<List<EventUIModel>> = _events

    init {
        loadAllEvents()
    }

    fun loadAllEvents(page: String = DEFAULT_PAGE) {
        viewModelScope.launch {
            when (val result = useCase(page = page)) {
                is Result.Success -> {
                    val eventResults = result.data.map { event ->
                        event.toEventUIModel()
                    } as MutableList
                    _events.value = (_events.value + eventResults) as MutableList<EventUIModel>
                }
                is Result.Failure -> {
                    _events.value = mutableListOf()
                }
            }
        }
    }

    private fun Event.toEventUIModel() = EventUIModel(
        title = title,
        organizer = organizer,
        startDate = startDate,
        endDate = endDate,
        logo = screenshot
    )
}
