package com.example.users.domain.useCases

import com.example.users.domain.models.User
import com.example.users.domain.repositories.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        repository.deleteUser(user)
    }
}