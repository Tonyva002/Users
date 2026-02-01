package com.example.users.ui.view.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.users.domain.models.User
import com.example.users.domain.useCases.DeleteUserUseCase
import com.example.users.domain.useCases.GetUserByIdUseCase
import com.example.users.ui.states.UserDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    private val deletedUser: DeleteUserUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)

    val uiState: StateFlow<UserDetailUiState> = _uiState

    fun loadUser(userId: Long) {
        viewModelScope.launch {
            _uiState.value = UserDetailUiState.Loading
            try {
                val user = getUserById(userId)
                _uiState.value = UserDetailUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UserDetailUiState.Error("User not found")
            }
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            deletedUser(user)
        }
    }
}