package ru.fintech.spring.task6.entity

import ru.fintech.spring.task6.dto.UserDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    val name: String,
    val username: String,
    val email: String,
    var quote: String?
)

fun UserEntity.toDto() = UserDto(id, name, username, email, quote)