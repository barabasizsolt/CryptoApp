package com.example.cryptoapp.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.domain.news.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
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
            listOf(NewsListItem.ErrorState())
        } else {
            if (news == null ) {
                emptyList()
            } else {
                news.map { it.toListItem() } + NewsListItem.LoadMore()
            }
        }
    }

    init {
        refreshData(false)
    }

    fun refreshData(isForceRefresh: Boolean) {
        if (!_isRefreshing.value) {
            viewModelScope.launch {
                _isRefreshing.value = true
                shouldShowError.value = false
                when (val result = useCase(
                    refreshType = when {
                        isForceRefresh -> RefreshType.FORCE_REFRESH
                        news.value.isNullOrEmpty() -> RefreshType.CACHE_IF_POSSIBLE
                        else -> RefreshType.NEXT_PAGE
                    }
                )) {
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

    fun onNewsItemClicked(url: String) {
        // TODO
    }

    private fun News.toListItem() = NewsListItem.News(
        title = title,
        site = site,
        updated = updated,
        url = url,
        logo = logo
    )
}
