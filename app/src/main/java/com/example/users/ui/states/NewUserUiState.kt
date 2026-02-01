package com.example.users.ui.states

import com.example.users.domain.models.User

sealed class NewUserUiState {
    object Idle : NewUserUiState()
    object Loading : NewUserUiState()
    object Saved : NewUserUiState()
    data class Success(val user: User) : NewUserUiState()
    data class Error(val message: String) : NewUserUiState()
}