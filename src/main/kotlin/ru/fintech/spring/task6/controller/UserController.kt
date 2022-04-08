package ru.fintech.spring.task6.controller

import org.springframework.web.bind.annotation.*
import ru.fintech.spring.task6.dto.UserDto
import ru.fintech.spring.task6.dto.toEntity
import ru.fintech.spring.task6.entity.UserEntity
import ru.fintech.spring.task6.entity.toDto
import ru.fintech.spring.task6.service.UserService

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {


    @PostMapping
    fun addUser(@RequestBody userDto: UserDto): Long {

        return userService.addUser(userDto.toEntity())
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): UserDto {

        return userService.getUser(userId).toDto()
    }

    @GetMapping
    fun getUsers(
        @RequestParam page: Int?,
        @RequestParam size: Int?,
        @RequestParam name: String?,
        @RequestParam username: String?,
        @RequestParam email: String?
    ): List<UserDto> {


        return userService.getUsers(
            page = page ?: DEFAULT_PAGE,
            size = size ?: DEFAULT_PAGE_SIZE,
            name = name,
            username = username,
            email = email
        ).map(UserEntity::toDto)
    }

    companion object {
        private const val DEFAULT_PAGE = 0
        private const val DEFAULT_PAGE_SIZE = 3
    }
}

