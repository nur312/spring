package ru.fintech.spring.task6.dto

import ru.fintech.spring.task6.entity.User


data class UserDto(
    var id: Long,
    val name: String,
    val username: String,
    val email: String,
    val quote: String?
) {
    constructor(name: String, username: String, email: String) : this(0, name, username, email, null)
}

fun UserDto.toEntity() = User(id, name, username, email, quote)

