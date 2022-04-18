package ru.fintech.spring.task6.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import ru.fintech.spring.task6.entity.User

@Repository
interface UserRepositoryJpa : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}