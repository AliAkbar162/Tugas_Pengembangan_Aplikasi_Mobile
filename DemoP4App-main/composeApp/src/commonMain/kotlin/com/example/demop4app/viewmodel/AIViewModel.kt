package com.example.demop4app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demop4app.data.model.AIError
import com.example.demop4app.data.repository.AIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AIState {
    object Idle : AIState()
    object Loading : AIState()
    data class Success(val summary: String) : AIState()
    data class Error(val message: String) : AIState()
}

class AIViewModel(private val aiRepository: AIRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AIState>(AIState.Idle)
    val uiState: StateFlow<AIState> = _uiState.asStateFlow()

    fun summarizeNote(content: String) {
        if (content.isBlank()) {
            _uiState.value = AIState.Error("Note content is empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = AIState.Loading
            aiRepository.getSummary(content)
                .onSuccess { summary ->
                    _uiState.value = AIState.Success(summary)
                }
                .onFailure { exception ->
                    val errorMessage = when (exception) {
                        is AIError.InvalidApiKey -> "Invalid API Key. Please check your configuration."
                        is AIError.RateLimitExceeded -> "Too many requests. Please try again later."
                        is AIError.ServerError -> "Server error. Please try again later."
                        is AIError.Timeout -> "Request timed out. Please check your connection."
                        else -> exception.message ?: "An unknown error occurred"
                    }
                    _uiState.value = AIState.Error(errorMessage)
                }
        }
    }

    fun resetState() {
        _uiState.value = AIState.Idle
    }
}
