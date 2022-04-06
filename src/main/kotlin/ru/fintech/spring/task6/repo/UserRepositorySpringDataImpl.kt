package ru.fintech.spring.task6.repo

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import ru.fintech.spring.task6.entity.UserEntity
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class UserRepositorySpringDataImpl(private val userRepo: UserEntitySpringDataRepository) : Repo<UserEntity, Long> {
    override fun save(entity: UserEntity): UserEntity {
        entity.id = userRepo.save(entity).id
        return entity
    }

    override fun findByIdOrNull(id: Long): UserEntity? = userRepo.findByIdOrNull(id)

    override fun count(): Int = userRepo.count().toInt()

    override fun findAll(page: Int, size: Int, name: String?, username: String?, email: String?): List<UserEntity> {
        return userRepo.findAll(
            { root: Root<UserEntity>, _, criteriaBuilder: CriteriaBuilder ->
                val predicates: MutableList<Predicate> = ArrayList()
                if (name != null) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "$name%"))
                }
                if (username != null) {
                    predicates.add(criteriaBuilder.like(root.get("username"), "$username%"))
                }
                if (email != null) {
                    predicates.add(criteriaBuilder.like(root.get("email"), "$email%"))
                }

                criteriaBuilder.and(*predicates.toTypedArray())
            },
            PageRequest.of(page, size, Sort.by("id"))
        ).toList()
    }

    override fun deleteAll() {
        userRepo.deleteAll()
    }
}