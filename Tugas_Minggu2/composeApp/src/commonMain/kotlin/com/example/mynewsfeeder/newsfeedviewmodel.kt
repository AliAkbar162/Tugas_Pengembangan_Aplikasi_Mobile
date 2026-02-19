package com.example.mynewsfeeder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    private val repository: NewsRepository = NewsRepository()
) {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _newsFeed = MutableStateFlow<List<NewsItem>>(emptyList())
    val newsFeed: StateFlow<List<NewsItem>> = _newsFeed.asStateFlow()

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val _activeFilter = MutableStateFlow<NewsCategory?>(null)
    val activeFilter: StateFlow<NewsCategory?> = _activeFilter.asStateFlow()

    private val _filteredFeed = MutableStateFlow<List<NewsItem>>(emptyList())
    val filteredFeed: StateFlow<List<NewsItem>> = _filteredFeed.asStateFlow()

    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    private val _selectedDetail = MutableStateFlow<NewsDetail?>(null)
    val selectedDetail: StateFlow<NewsDetail?> = _selectedDetail.asStateFlow()

    private val _statusMessage = MutableStateFlow("Tekan Mulai Stream untuk memulai.")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    private var newsStreamJob: Job? = null

    private fun updateFilteredFeed() {
        val filter = _activeFilter.value
        _filteredFeed.value = if (filter == null) {
            _newsFeed.value
        } else {
            _newsFeed.value.filter { it.category == filter }
        }
    }

    fun startNewsStream() {
        newsStreamJob?.cancel()
        _newsFeed.value = emptyList()
        _filteredFeed.value = emptyList()
        _readCount.value = 0
        _statusMessage.value = "Streaming berita dimulai..."

        newsStreamJob = viewModelScope.launch {
            repository.newsFlow()
                .onEach { news ->
                    _statusMessage.value = "Berita baru masuk: ${news.title}"
                }
                .catch { error ->
                    _statusMessage.value = "Error: ${error.message}"
                }
                .collect { news ->
                    val updated = _newsFeed.value.toMutableList()
                    updated.add(0, news)
                    _newsFeed.value = updated
                    updateFilteredFeed()
                }

            _statusMessage.value = "Streaming selesai. ${_newsFeed.value.size} berita diterima."
        }
    }

    fun stopNewsStream() {
        newsStreamJob?.cancel()
        _statusMessage.value = "Streaming dihentikan."
    }

    fun setFilter(category: NewsCategory?) {
        _activeFilter.value = category
        updateFilteredFeed()
        _statusMessage.value = "Filter: ${category?.name ?: "SEMUA"}"
    }

    fun openNewsDetail(newsId: Int) {
        viewModelScope.launch {
            _isLoadingDetail.value = true
            _selectedDetail.value = null

            val detailDeferred = async { repository.fetchNewsDetail(newsId) }
            val detail = detailDeferred.await()

            _selectedDetail.value = detail
            _isLoadingDetail.value = false

            if (detail != null) {
                _readCount.value++
                _statusMessage.value = "Membaca: ${detail.newsItem.title}"
            }
        }
    }

    fun closeDetail() {
        _selectedDetail.value = null
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}
