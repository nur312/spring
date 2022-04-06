package ru.fintech.spring.task6.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.fintech.spring.task6.entity.UserEntity

interface UserEntitySpringDataRepository : JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
}