package com.example.users.data.local.mapper

import com.example.users.data.local.entity.UserEntity
import com.example.users.domain.models.User


fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    lastname = lastname,
    company = company,
    age = age,
    email = email,
    phone = phone,
    weight = weight,
    address = address,
    photoResId = photoResId,
    website = website,
    isFavorite = isFavorite,
)

fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    lastname = lastname,
    company = company,
    age = age,
    email = email,
    phone = phone,
    weight = weight,
    address = address,
    photoResId = photoResId,
    website = website,
    isFavorite = isFavorite
)



