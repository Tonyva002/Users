package com.example.users.domain.models

data class User(
    val id: Long,
    val name: String,
    val lastname: String,
    val company: String,
    val age: Int,
    val email: String,
    val phone: String,
    val weight: Double,
    val address: String,
    val photoResId: Int,
    val website: String,
    val isFavorite: Boolean,

)