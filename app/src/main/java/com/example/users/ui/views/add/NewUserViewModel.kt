package com.example.users.ui.views.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.users.domain.models.User
import com.example.users.domain.useCases.AddUserUseCase
import com.example.users.domain.useCases.GetUserByIdUseCase
import com.example.users.domain.useCases.UpdateUserUseCase
import com.example.users.ui.states.NewUserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewUserViewModel @Inject constructor(
    private val getUserById: GetUserByIdUseCase,
    private val addUser: AddUserUseCase,
    private val updateUser: UpdateUserUseCase

) : ViewModel(){
    private val _uiState = MutableStateFlow<NewUserUiState>(NewUserUiState.Idle)
    val uiState: StateFlow<NewUserUiState> = _uiState


    fun loadUser(userId: Long) {
        viewModelScope.launch {
            _uiState.value = NewUserUiState.Loading
            try {
                _uiState.value = NewUserUiState.Success(getUserById(userId))
            }catch (e: Exception) {
                _uiState.value = NewUserUiState.Error(e.message ?: "User not found")
            }
        }
    }

    fun save(user: User, isEdit: Boolean){
        viewModelScope.launch {
            try {
                if (isEdit) updateUser(user) else addUser(user)
            }catch (e: Exception){
                _uiState.value = NewUserUiState.Error(e.message ?: "Error saving user")
            }
        }
    }
}