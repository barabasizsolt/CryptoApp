package com.example.cryptoapp.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.NewsConstant.DEFAULT_PAGE
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.domain.news.GetNewsUseCase
import com.example.cryptoapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val useCase: GetNewsUseCase) : ViewModel() {
    private val _news = MutableStateFlow(emptyList<NewsUIModel>())
    val news: Flow<List<NewsUIModel>> = _news

    init {
        loadAllNews()
    }

    fun loadAllNews(page: String = DEFAULT_PAGE) {
        viewModelScope.launch {
            when (val result = useCase(page = page)) {
                is Result.Success -> {
                    val newsResults = result.data.map { event ->
                        event.toEventUIModel()
                    } as MutableList
                    _news.value = (_news.value + newsResults) as MutableList<NewsUIModel>
                }
                is Result.Failure -> {
                    _news.value = mutableListOf()
                }
            }
        }
    }

    private fun News.toEventUIModel() = NewsUIModel(
        title = title,
        site = site,
        updated = updated,
        logo = logo
    )
}
