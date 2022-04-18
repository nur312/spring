package ru.fintech.spring.task6.repo

import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.fintech.spring.task6.entity.User
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Types

// По условии нужно было так сделать
// "В итоговом приложении инжектить любой из из них(пометить один из них с помощью @Primary)"
@Primary
@Repository
class UserDao(private val jdbcTemplate: JdbcTemplate) : Repo<User, Long> {


    override fun save(entity: User): User {
        val keyHolder: KeyHolder = GeneratedKeyHolder()


        val numOfUpdates = jdbcTemplate.update({ connection: Connection ->
            val ps = connection
                .prepareStatement(
                    "insert into users (name, username, email, quote) values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                )
            ps.setString(1, entity.name)
            ps.setString(2, entity.username)
            ps.setString(3, entity.email)
            ps.setString(4, entity.quote)
            ps
        }, keyHolder)

        require(numOfUpdates == 1)

        entity.id = keyHolder.key as Long

        return entity
    }

    override fun findByIdOrNull(id: Long): User? {

        return jdbcTemplate.query(
            "select * from users where id = ?",
            arrayOf<Any>(id),
            intArrayOf(Types.BIGINT)
        ) { rs: ResultSet, _: Int ->
            User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("quote"),
            )
        }.firstOrNull()
    }

    override fun count(): Int {
        return jdbcTemplate
            .queryForObject("select count(*) from users", Int::class.java) ?: 0
    }

    override fun findAll(page: Int, size: Int, name: String?, username: String?, email: String?): List<User> {

        val sql = "select * from users where 1=1 " +
                (if (!name.isNullOrBlank()) "and name like '$name%' " else "") +
                (if (!username.isNullOrBlank()) "and username like '${username}%' " else "") +
                (if (!email.isNullOrBlank()) "and email like '${email}%' " else "") +
                "order by id " +
                "offset ${page * size}  rows " +
                "fetch first $size rows only"

        return jdbcTemplate.query(sql) { rs: ResultSet, _: Int ->
            User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("quote"),
            )
        }
    }

    override fun deleteAll() {
        jdbcTemplate.update("delete users")
    }
}