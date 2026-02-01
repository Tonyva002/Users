package com.example.users.domain.useCases

import com.example.users.domain.models.User
import com.example.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetUsersUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(): Flow<List<User>> = repository.getUsers()
}