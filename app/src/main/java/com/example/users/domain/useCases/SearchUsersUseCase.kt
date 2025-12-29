package com.example.users.domain.useCases

import com.example.users.domain.models.User
import com.example.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(query: String): Flow<List<User>> {
        return repository.searchUsers(query)
    }
}