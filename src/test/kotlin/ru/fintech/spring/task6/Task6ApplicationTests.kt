package ru.fintech.spring.task6

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.fintech.spring.task6.client.RandomQuoteClient
import ru.fintech.spring.task6.dto.UserDto
import ru.fintech.spring.task6.dto.toEntity
import ru.fintech.spring.task6.entity.User
import ru.fintech.spring.task6.repo.Repo
import ru.fintech.spring.task6.repo.UserDao
import ru.fintech.spring.task6.repo.UserRepository
import ru.fintech.spring.task6.service.UserService


@Suppress("LeakingThis")
@SpringBootTest
@AutoConfigureMockMvc
class Task6ApplicationTests(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    repo: Repo<User, Long>,
    jdbcRepo: UserDao,
    springDataRepo: UserRepository
) : FeatureSpec() {

    private val quoteMock = mockk<RandomQuoteClient>()

    override fun beforeEach(testCase: TestCase) {
        every { quoteMock.getQuote() } returns "I'm a quote"
    }

    override fun afterEach(testCase: TestCase, result: TestResult) {
        clearAllMocks()
    }

    init {
        feature("spring jdbc repo") {
            val messi = User("Messi", "mes", "mes@mes.sp")

            scenario("add") {
                messi.id = jdbcRepo.save(messi).id

                messi.id shouldNotBe 0L
            }

            scenario("count") {
                val count = jdbcRepo.count()

                count shouldBe 1
            }

            scenario("get") {
                val read = jdbcRepo.findByIdOrNull(messi.id)

                requireNotNull(read)

                read should {
                    it.id shouldBe messi.id
                    it.name shouldBe messi.name
                    it.username shouldBe messi.username
                    it.email shouldBe messi.email
                    it.quote shouldBe null
                }
            }

            for (dto in initialUsers) {
                jdbcRepo.save(dto.toEntity())
            }

            scenario("find #1") {
                val expected = listOf(
                    UserDto("James", "james", "James@mail.com"),
                    UserDto("John", "john", "Nohn@mail.com"),
                )

                val result = jdbcRepo.findAll(0, 2, "J", "j", null).map { UserDto(it.name, it.username, it.email) }

                result shouldBe expected
            }

            scenario("find #2") {
                val expected = listOf(
                    UserDto("Jennifer", "jennifer", "Jennifer@mail.com"),
                )

                val result = jdbcRepo.findAll(1, 2, "J", "j", null)
                    .map { UserDto(it.name, it.username, it.email) }

                result shouldBe expected
            }

            jdbcRepo.deleteAll()
        }
        feature("spring data repo") {
            val messi = User("Messi", "mes", "mes@mes.sp")

            scenario("add") {
                messi.id = springDataRepo.save(messi).id

                messi.id shouldNotBe 0L
            }

            scenario("count") {
                val count = springDataRepo.count()

                count shouldBe 1
            }

            scenario("get") {
                val read = springDataRepo.findByIdOrNull(messi.id)

                requireNotNull(read)

                read should {
                    it.id shouldBe messi.id
                    it.name shouldBe messi.name
                    it.username shouldBe messi.username
                    it.email shouldBe messi.email
                    it.quote shouldBe null
                }
            }

            for (dto in initialUsers) {
                springDataRepo.save(dto.toEntity())
            }

            scenario("find #1") {
                val expected = listOf(
                    UserDto("James", "james", "James@mail.com"),
                    UserDto("John", "john", "Nohn@mail.com"),
                )

                val result =
                    springDataRepo.findAll(0, 2, "J", "j", null).map { UserDto(it.name, it.username, it.email) }

                result shouldBe expected
            }

            scenario("find #2") {
                val expected = listOf(
                    UserDto("Jennifer", "jennifer", "Jennifer@mail.com"),
                )

                val result = springDataRepo.findAll(1, 2, "J", "j", null)
                    .map { UserDto(it.name, it.username, it.email) }

                result shouldBe expected
            }

            springDataRepo.deleteAll()
        }
        feature("service") {


            val userService = UserService(quoteMock, repo)

            scenario("add users and working with the client") {
                for (dto in initialUsers) {
                    userService.addUser(dto.toEntity())
                }
                val expected = initialUsers.map { UserDto(0, it.name, it.username, it.email, "I'm a quote") }

                val result = userService.getUsers(0, 10).map { UserDto(0, it.name, it.username, it.email, it.quote) }

                result shouldBe expected
            }

            scenario("page number and size") {
                shouldThrow<IllegalArgumentException> { userService.getUsers(-1, 0) }
            }
        }
        feature("controller") {

            val messi = UserDto("Messi", "mes", "mes@mes.sp")

            scenario("add") {
                messi.id = addUser(messi)

                messi.id shouldNotBe 0L
            }

            scenario("get") {
                val read = getUser(messi.id)

                read should {
                    it.id shouldBe messi.id
                    it.name shouldBe messi.name
                    it.username shouldBe messi.username
                    it.email shouldBe messi.email
                    it.quote shouldNotBe null
                }
            }

            for (dto in initialUsers) {
                addUser(dto)
            }

            scenario("find #1") {
                val expected = listOf(
                    UserDto("James", "james", "James@mail.com"),
                    UserDto("John", "john", "Nohn@mail.com"),
                )

                val result = find(0, 2, "J", "j").map { UserDto(it.name, it.username, it.email) }

                result shouldBe expected
            }

            scenario("find #2") {
                val expected = listOf(
                    UserDto("Jennifer", "jennifer", "Jennifer@mail.com"),
                    UserDto("James", "james", "James@mail.com"),
                )

                val result = find(1, 2, "J", "j")
                    .map { UserDto(it.name, it.username, it.email) }

                result shouldBe expected
            }

            scenario("exception") {
                mockMvc.get(
                    "/users?page={page}&size={size}",
                    -1,
                    0
                ).andExpect { status { isEqualTo(HttpStatus.BAD_REQUEST.value()) } }
            }
        }
    }

    private fun addUser(userDto: UserDto) =
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().response.contentAsString.toLong()

    private fun getUser(id: Long, status: HttpStatus = HttpStatus.OK): UserDto =
        mockMvc.get("/users/{id}", id).readResponse(status)


    private fun find(
        page: Int,
        size: Int,
        name: String,
        username: String,
        status: HttpStatus = HttpStatus.OK
    ): List<UserDto> =
        mockMvc.get(
            "/users?page={page}&size={size}&name={name}&username={username}",
            page,
            size,
            name,
            username
        ).readResponse(status)


    private inline fun <reified T> ResultActionsDsl.readResponse(expectedStatus: HttpStatus = HttpStatus.OK): T = this
        .andExpect { status { isEqualTo(expectedStatus.value()) } }
        .andReturn().response.getContentAsString(Charsets.UTF_8)
        .let { if (T::class == String::class) it as T else objectMapper.readValue(it) }

    private val initialUsers = listOf(
        UserDto("James", "james", "James@mail.com"),
        UserDto("Mary", "mary", "Mary@mail.com"),
        UserDto("Robert", "robert", "Robert@mail.com"),
        UserDto("John", "john", "Nohn@mail.com"),
        UserDto("Jennifer", "jennifer", "Jennifer@mail.com"),
        UserDto("Michael", "michael", "Michael@mail.com"),
        UserDto("William", "william", "William@mail.com"),
        UserDto("David", "david", "David@mail.com"),
        UserDto("Karen", "karen", "Karen@mail.com"),
    )
}


