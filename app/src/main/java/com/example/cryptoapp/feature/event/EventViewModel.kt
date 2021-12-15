package com.example.cryptoapp.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.EventConstant.DEFAULT_PAGE
import com.example.cryptoapp.data.constant.EventConstant.toEventUIModel
import com.example.cryptoapp.data.model.event.EventUIModel
import com.example.cryptoapp.domain.event.GetEventsUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val useCase: GetEventsUseCase) : ViewModel() {
    val events = MutableStateFlow<MutableList<EventUIModel>>(mutableListOf())

    fun loadAllEvents(page: String = DEFAULT_PAGE) {
        viewModelScope.launch {
            when (val result = useCase(page = page)) {
                is Result.Success -> {
                    val eventResults = result.data.map { event ->
                        event.toEventUIModel()
                    } as MutableList
                    events.value = (events.value + eventResults) as MutableList<EventUIModel>
                }
                is Result.Failure -> {
                    events.value = mutableListOf()
                }
            }
        }
    }

    init {
        loadAllEvents()
    }
}
