package com.example.users.domain.repositories

import com.example.users.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    fun searchUsers(query: String): Flow<List<User>>

    suspend fun getUserById(id: Long): User
    suspend fun addUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}