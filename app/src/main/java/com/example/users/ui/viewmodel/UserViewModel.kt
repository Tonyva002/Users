package com.example.users.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.users.domain.models.User
import com.example.users.domain.useCases.*
import com.example.users.ui.state.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsers: GetUsersUseCase,
    private val searchUsers: SearchUsersUseCase,
    private val addUser: AddUserUseCase,
    private val updateUser: UpdateUserUseCase,
    private val deleteUser: DeleteUserUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        observeUsers()
    }

    fun observeUsers() {
        viewModelScope.launch {
            getUsers().collect { users ->
                _uiState.value = UserUiState.Success(users)
            }
        }
    }


    fun search(query: String) {
        viewModelScope.launch {
            try {
                searchUsers(query).collect { users ->
                    _uiState.value = UserUiState.Success(users)
                    }
            } catch (e: Exception) {
                _uiState.value =
                    UserUiState.Error(e.message ?: "Error al buscar usuarios")
            }
        }
    }

    fun add(user: User) {
        viewModelScope.launch {
            try {
                addUser(user)
            } catch (e: Exception) {
                _uiState.value =
                    UserUiState.Error(e.message ?: "Error al agregar usuario")
            }
        }
    }

    fun update(user: User) {
        viewModelScope.launch {
            try {
                updateUser(user)
            } catch (e: Exception) {
                _uiState.value =
                    UserUiState.Error(e.message ?: "Error al actualizar usuario")
            }
        }
    }

    fun delete(user: User) {
        viewModelScope.launch {
            try {
                deleteUser(user)
            } catch (e: Exception) {
                _uiState.value =
                    UserUiState.Error(e.message ?: "Error al eliminar usuario")
            }
        }
    }
}
