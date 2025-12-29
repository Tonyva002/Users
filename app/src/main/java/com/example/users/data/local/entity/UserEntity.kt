package com.example.users.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val lastname: String,
    val company: String,
    val age: Int,
    val email: String,
    val phone: String,
    val weight: Double,
    val address: String,
    val photoResId: Int,
    val website: String = "",
    val isFavorite: Boolean = false)