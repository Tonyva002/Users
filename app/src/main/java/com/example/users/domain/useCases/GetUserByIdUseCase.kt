package com.example.users.domain.useCases

import com.example.users.domain.repositories.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Long) = repository.getUserById(id)
}