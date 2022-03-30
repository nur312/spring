package ru.fintech.spring.task6.repo

import org.springframework.stereotype.Repository
import ru.fintech.spring.task6.entity.UserEntity
import java.util.*
import kotlin.collections.ArrayList

/*
* Этот класс не тестировал, так как это работа с бд (как-бы мы готовое испоользуем)
* */
@Repository
class UserRepoImpl : Repo<UserEntity, Long> {

    private val entities: MutableList<UserEntity> = Collections.synchronizedList(ArrayList<UserEntity>())

    override fun save(entity: UserEntity): UserEntity {
        entity.id = (entities.size + 1).toLong()
        entities.add(entity)
        return entity
    }

    override fun findById(id: Long): UserEntity? = entities.find { it.id == id }

    override fun count(): Int = entities.size

    override fun findAll(filter: (UserEntity) -> Boolean, page: Int, size: Int) =
        entities.asSequence().filter(filter).sortedBy { it.id }.drop(page * size).take(size).toList()
}
