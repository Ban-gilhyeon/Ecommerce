package small.ecommerce.order

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import small.ecommerce.domain.Brand.Brand
import small.ecommerce.domain.Brand.BrandRepository
import small.ecommerce.domain.enums.Category
import small.ecommerce.domain.enums.Gender
import small.ecommerce.domain.product.Product
import small.ecommerce.domain.product.ProductRepository
import small.ecommerce.domain.product.ProductSize
import small.ecommerce.domain.user.User
import small.ecommerce.domain.user.UserRepository
import small.ecommerce.domain.user.UserRole
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
class OrderConcurrencyTest {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var brandRepository: BrandRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var productId: Long = 0L

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .build()

        val buyer = userRepository.save(
            User(
                email = "buyer-concurrency@test.com",
                password = "pw",
                name = "buyer",
                role = UserRole.BUYER,
                address = "Seoul",
            )
        )

        val seller = userRepository.save(
            User(
                email = "seller-concurrency@test.com",
                password = "pw",
                name = "seller",
                role = UserRole.SELLER,
                address = "Seoul",
            )
        )

        val brand = brandRepository.save(
            Brand(
                name = "concurrency-brand",
                description = "test",
                owner = seller,
            )
        )

        val product = productRepository.save(
            Product(
                name = "concurrency-product",
                price = 1000,
                brand = brand,
                stock = 10000,
                category = Category.OUTER,
                gender = Gender.UNISEX,
                size = ProductSize.M,
            )
        )

        productId = product.id
        testAuthentication = UsernamePasswordAuthenticationToken(
            small.ecommerce.domain.auth.dto.UserPrincipal.from(buyer.id, buyer.role),
            null,
            listOf(SimpleGrantedAuthority(buyer.role.name)),
        )
    }

    private lateinit var testAuthentication: UsernamePasswordAuthenticationToken

    @Test
    fun same_product_300_concurrent_orders_should_complete_without_timeout_and_reduce_stock() {
        val concurrency = 300
        val ready = CountDownLatch(concurrency)
        val start = CountDownLatch(1)
        val done = CountDownLatch(concurrency)

        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)
        val errors = ConcurrentLinkedQueue<String>()

        val requestJson = objectMapper.writeValueAsString(
            mapOf(
                "itemInfoList" to listOf(
                    mapOf(
                        "productId" to productId,
                        "quantity" to 1,
                        "couponIssueId" to null,
                    )
                )
            )
        )

        val executor = Executors.newFixedThreadPool(64)
        val startedAt = System.nanoTime()

        repeat(concurrency) {
            executor.submit {
                try {
                    ready.countDown()
                    start.await(10, TimeUnit.SECONDS)

                    val response = mockMvc.perform(
                        post("/api/v1/order")
                            .with(authentication(testAuthentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson)
                    ).andReturn().response

                    if (response.status == 200) {
                        successCount.incrementAndGet()
                    } else {
                        failCount.incrementAndGet()
                        errors.add("HTTP_${response.status}")
                    }
                } catch (e: Exception) {
                    failCount.incrementAndGet()
                    errors.add(e.javaClass.simpleName + ":" + (e.cause?.javaClass?.simpleName ?: "-"))
                } finally {
                    done.countDown()
                }
            }
        }

        assertTrue(ready.await(10, TimeUnit.SECONDS), "동시 요청 준비 단계에서 타임아웃 발생")
        start.countDown()
        assertTrue(done.await(180, TimeUnit.SECONDS), "동시 요청 처리 단계에서 타임아웃 발생")

        executor.shutdown()
        executor.awaitTermination(30, TimeUnit.SECONDS)

        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
        val errorSummary = errors.groupingBy { it }.eachCount().toString()

        assertEquals(0, failCount.get(), "실패 요청 존재. elapsedMs=$elapsedMs, errors=$errorSummary")
        assertEquals(concurrency, successCount.get(), "성공 요청 수가 기대값과 다름. elapsedMs=$elapsedMs")

        val finalStock = productRepository.findById(productId).orElseThrow().stock
        assertEquals(10000 - concurrency, finalStock, "최종 재고가 기대값과 다름")

        val timeoutErrors = errors.filter {
            it.contains("Timeout", ignoreCase = true) ||
                it.contains("Connection is not available", ignoreCase = true) ||
                it.contains("CannotAcquireLock", ignoreCase = true)
        }
        assertTrue(timeoutErrors.isEmpty(), "타임아웃/락 획득 실패 계열 에러 발생: $timeoutErrors")
    }
}
