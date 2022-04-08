package ru.fintech.spring.task6.service

import org.springframework.stereotype.Service
import ru.fintech.spring.task6.client.RandomQuoteClient
import ru.fintech.spring.task6.entity.UserEntity
import ru.fintech.spring.task6.repo.Repo

@Service
class UserService(private val userRepo: Repo<UserEntity, Long>, private val quoteClient: RandomQuoteClient) {

    fun addUser(userEntity: UserEntity): Long {

        userEntity.quote = quoteClient.getQuote()
        return userRepo.save(userEntity).id
    }

    fun getUser(id: Long): UserEntity {

        return userRepo.findById(id) ?: throw IllegalArgumentException("no id = $id in database")
    }

    fun getUsers(
        page: Int, size: Int, name: String? = null, username: String? = null, email: String? = null
    ): List<UserEntity> {
        require(page >= 0 && size > 0) { "request's params are illegal" }

        val filter: (UserEntity) -> (Boolean) = {
            (name == null || it.name.startsWith(name)) &&
                    (username == null || it.username.startsWith(username)) &&
                    (email == null || it.email.startsWith(email))
        }

        return userRepo.findAll(filter, page, size)
    }

}