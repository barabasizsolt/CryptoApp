package com.example.cryptoapp.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.EventConstant
import com.example.cryptoapp.data.constant.EventConstant.toEventUIModel
import com.example.cryptoapp.data.model.event.EventUIModel
import com.example.cryptoapp.domain.event.GetEventsUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val useCase: GetEventsUseCase) : ViewModel() {
    val events = MutableStateFlow<List<EventUIModel>?>(listOf())

    fun loadAllEvents(page: String = EventConstant.PAGE) {
        viewModelScope.launch {
            when (val result = useCase(page = page)) {
                is Result.Success -> {
                    events.value = result.data.map { event ->
                        event.toEventUIModel()
                    }
                }
                is Result.Failure -> {
                    events.value = listOf()
                }
            }
        }
    }

    init {
        loadAllEvents()
    }
}
