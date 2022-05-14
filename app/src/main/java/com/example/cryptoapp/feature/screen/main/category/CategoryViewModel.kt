package com.example.cryptoapp.feature.screen.main.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.data.model.category.Category
import com.example.cryptoapp.domain.category.GetCategoriesUseCase
import com.example.cryptoapp.feature.shared.utils.convertToCompactPrice
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CategoryViewModel(private val getCategoriesUseCase: GetCategoriesUseCase) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val categories = MutableStateFlow<List<Category>?>(null)

    private val shouldShowError = MutableStateFlow(false)

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    val listItem = combine(categories, shouldShowError) { categories, shouldShowError ->
        if (shouldShowError) {
            categories?.map { it.toListItem() } ?: listOf(CategoryListItem.ErrorState())
        } else {
            when {
                categories == null || categories.isEmpty() -> emptyList()
                else -> categories.map { it.toListItem() }
            }
        }
    }

    init {
        refreshData(isForceRefresh = false)
    }

    fun refreshData(isForceRefresh: Boolean) {
        _isRefreshing.value = true
        shouldShowError.value = false
        viewModelScope.launch {
            when (
                val result = getCategoriesUseCase(
                    refreshType = when {
                        isForceRefresh -> RefreshType.FORCE_REFRESH
                        else -> RefreshType.CACHE_IF_POSSIBLE
                    }
                )
            ) {
                is Result.Success -> {
                    categories.value = result.data
                    _isRefreshing.value = false
                }
                is Result.Failure -> {
                    _isRefreshing.value = false
                    shouldShowError.value = true
                    _event.pushEvent(event = Event.ShowErrorMessage(errorMessage = result.exception.message.toString()))
                }
            }
        }
    }

    private fun Category.toListItem() = CategoryListItem.Category(
        categoryId = id,
        name = name,
        top3CoinLogos = top3Coins,
        volume = marketCapChange24h,
        marketCap = marketCap.toString().convertToCompactPrice()
    )

    sealed class Event {

        data class ShowErrorMessage(val errorMessage: String) : Event()
    }
}
