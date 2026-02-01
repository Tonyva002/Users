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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsers: GetUsersUseCase,
    private val searchUsers: SearchUsersUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UserUiState> =
        query
            .flatMapLatest{ text ->
                if (text.isBlank()) getUsers()
                else searchUsers(text)
            }
            .map<List<User>, UserUiState> { users ->
                UserUiState.Success(users)
            }
            .onStart { emit(UserUiState.Loading)
            }
            .catch { e ->
                emit(UserUiState.Error(e.message ?: "Unexpected error"))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserUiState.Loading
            )


    fun onSearch(text: String) {
        query.value = text
    }

    fun refresh() {
        query.value = query.value
    }

}