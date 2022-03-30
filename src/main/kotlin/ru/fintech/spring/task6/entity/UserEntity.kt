package ru.fintech.spring.task6.entity

import ru.fintech.spring.task6.dto.UserDto


data class UserEntity(
    var id: Long,
    val name: String,
    val username: String,
    val email: String,
    var quote: String?
)

fun UserEntity.toDto() = UserDto(id, name, username, email, quote)