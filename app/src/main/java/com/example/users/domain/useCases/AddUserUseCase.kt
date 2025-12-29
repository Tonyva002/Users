package com.example.users.domain.useCases

import com.example.users.domain.models.User
import com.example.users.domain.repositories.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        repository.addUser(user)
    }
}