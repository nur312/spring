package ru.fintech.spring.task6.service

import org.springframework.stereotype.Service
import ru.fintech.spring.task6.client.RandomQuoteClient
import ru.fintech.spring.task6.entity.UserEntity
import ru.fintech.spring.task6.repo.Repo

@Service
class UserService(
    private val quoteClient: RandomQuoteClient,
    private val repo: Repo<UserEntity, Long>
) {

    fun addUser(userEntity: UserEntity): Long {

        userEntity.quote = quoteClient.getQuote()
        //return userRepo.save(userEntity).id
        return repo.save(userEntity).id
    }

    fun getUser(id: Long): UserEntity {

        return repo.findByIdOrNull(id) ?: throw IllegalArgumentException("no id = $id in database")
        //return userRepo.findByIdOrNull(id) ?: throw IllegalArgumentException("no id = $id in database")
    }

    fun getUsers(
        page: Int,
        size: Int,
        name: String? = null,
        username: String? = null,
        email: String? = null
    ): List<UserEntity> {

        require(page >= 0 && size > 0) { "request's params are illegal" }

        return repo.findAll(page, size, name, username, email)


    }


}