package com.example.users.ui.view.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.users.domain.models.User
import com.example.users.domain.useCases.AddUserUseCase
import com.example.users.domain.useCases.GetUsersUseCase
import com.example.users.domain.useCases.SearchUsersUseCase
import com.example.users.domain.useCases.UpdateUserUseCase
import com.example.users.ui.states.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsers: GetUsersUseCase,
    private val searchUsers: SearchUsersUseCase,
    private val addUser: AddUserUseCase,
    private val updateUser: UpdateUserUseCase,
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    private var usersJobs: Job? = null

    init {
        loadUsers()
    }

    fun loadUsers() {
        collectUsers(getUsers())
    }

    fun search(query: String) {
        if (query.isBlank()) {
            loadUsers()

        } else {
            collectUsers(searchUsers(query))
        }
    }

    private fun collectUsers(flow: Flow<List<User>>) {
        usersJobs?.cancel()
        usersJobs = viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            flow
                .catch {
                    _uiState.value = UserUiState.Error("Error loading users")
                }
                .collectLatest { users ->
                    _uiState.value = UserUiState.Success(users)

                }
        }
    }


    fun add(user: User) = launchAction {
        addUser(user)

    }


    private fun launchAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (e: Exception) {
                _events.emit(e.message ?: "Ocurrio un error")
            }
        }
    }
}