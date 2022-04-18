package ru.fintech.spring.task6.entity

import ru.fintech.spring.task6.dto.UserDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    val name: String,
    val username: String,
    val email: String,
    var quote: String?
) {
    constructor(name: String, username: String, email: String) : this(0, name, username, email, null)
}

fun User.toDto() = UserDto(id, name, username, email, quote)