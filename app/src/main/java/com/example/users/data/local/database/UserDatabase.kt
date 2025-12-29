package com.example.users.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.users.data.local.dao.UserDao
import com.example.users.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}
