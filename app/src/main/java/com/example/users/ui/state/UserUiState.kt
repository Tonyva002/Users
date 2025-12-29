package com.example.users.ui.state

import com.example.users.domain.models.User

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<User>) : UserUiState()
    data class Error(val message: String) : UserUiState()

}