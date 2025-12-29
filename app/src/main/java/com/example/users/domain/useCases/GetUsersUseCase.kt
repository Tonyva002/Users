package com.example.users.domain.useCases

import com.example.users.domain.repositories.UserRepository
import javax.inject.Inject


class GetUsersUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke() = repository.getUsers()
}