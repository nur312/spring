package ru.fintech.spring.task6.repo

interface Repo<E, PK> {

    fun save(entity: E): E
    fun findByIdOrNull(id: PK): E?
    fun count(): Int
    fun findAll(
        page: Int, size: Int, name: String? = null, username: String? = null, email: String? = null
    ): List<E>

    fun deleteAll()
}