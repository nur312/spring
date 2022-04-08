package ru.fintech.spring.task6.repo

interface Repo<E, PK> {

    fun save(entity: E): E
    fun findById(id: PK): E?
    fun count(): Int
    fun findAll(filter: (E) -> (Boolean), page: Int, size: Int): List<E>
}