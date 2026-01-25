package com.example.users.data.repositories

import com.example.users.data.local.dao.UserDao
import com.example.users.data.mapper.toDomain
import com.example.users.data.mapper.toEntity
import com.example.users.domain.models.User
import com.example.users.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao): UserRepository {

    override fun getUsers(): Flow<List<User>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override  fun searchUsers(query: String): Flow<List<User>> =
        dao.searchByName(query).map { list -> list.map { it.toDomain() } }


    override suspend fun addUser(user: User) {
        dao.insert(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        dao.update(user.toEntity())
    }

    override suspend fun deleteUser(user: User) {
        dao.delete(user.toEntity())
    }


}