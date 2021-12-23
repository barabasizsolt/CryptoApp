package com.example.cryptoapp.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.domain.news.GetNewsUseCase
import com.example.cryptoapp.feature.shared.eventFlow
import com.example.cryptoapp.feature.shared.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class NewsViewModel(private val useCase: GetNewsUseCase) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing
    private val news = MutableStateFlow<List<News>?>(null)
    private val shouldShowError = MutableStateFlow(false)
    val listItems = combine(news, shouldShowError) { news, shouldShowError ->
        if (shouldShowError) {
            news?.map { it.toListItem() }?.also {
                _event.pushEvent(Event.ErrorEvent(errorMessage = "Failed to load news"))
            } ?: listOf(NewsListItem.ErrorState())
        } else {
            when {
                news == null -> listOf(NewsListItem.ErrorState())
                news.isEmpty() -> emptyList()
                else -> news.map { it.toListItem() } + NewsListItem.LoadMore()
            }
        }
    }

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    init {
        refreshData(isForceRefresh = false)
    }

    fun refreshData(isForceRefresh: Boolean) {
        if (!_isRefreshing.value) {
            viewModelScope.launch {
                _isRefreshing.value = true
                shouldShowError.value = false
                when (
                    val result = useCase(
                        refreshType = when {
                            isForceRefresh -> RefreshType.FORCE_REFRESH
                            news.value.isNullOrEmpty() -> RefreshType.CACHE_IF_POSSIBLE
                            else -> RefreshType.NEXT_PAGE
                        }
                    )
                ) {
                    is Result.Success -> {
                        news.value = result.data
                    }
                    is Result.Failure -> {
                        shouldShowError.value = true
                    }
                }
                _isRefreshing.value = false
            }
        }
    }

    fun onNewsItemClicked(url: String) = _event.pushEvent(Event.OpenBrowserEvent(url))

    private fun News.toListItem() = NewsListItem.News(
        title = title,
        site = site,
        updated = updated,
        url = url,
        logo = logo
    )

    sealed class Event {
        data class OpenBrowserEvent(val url: String) : Event()

        data class ErrorEvent(val errorMessage: String) : Event()
    }
}
