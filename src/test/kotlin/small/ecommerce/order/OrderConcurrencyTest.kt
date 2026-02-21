package small.ecommerce.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.Filter
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
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
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
    private lateinit var springSecurityFilterChain: Filter

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var brandRepository: BrandRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var productId: Long = 0L

    @BeforeEach
    fun setUp() {
        // GIVEN: 보안 필터 체인을 포함한 MockMvc를 구성해 인증 주체가 주입되도록 한다.
        val mockMvcBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        val configuredBuilder: DefaultMockMvcBuilder = mockMvcBuilder.addFilters(springSecurityFilterChain)
        mockMvc = configuredBuilder.build()

        // GIVEN: 주문 테스트에 필요한 구매자/판매자/브랜드/상품을 미리 저장한다.
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

        // GIVEN: 브랜드 생성은 판매자 계정이 소유자가 되어야 한다.
        val brand = brandRepository.save(
            Brand(
                name = "concurrency-brand",
                description = "test",
                owner = seller,
            )
        )

        // GIVEN: 재고가 충분한 상품을 생성해 동시 주문 테스트에 사용한다.
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

        // GIVEN: 테스트에서 사용할 상품 ID와 인증 토큰을 준비한다.
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
        // GIVEN: 동시에 주문을 300건 발행한다.
        val concurrency = 3000
        // GIVEN: 모든 작업이 준비될 때까지 기다리는 래치(ready), 동시에 시작시키는 래치(start),
        //        모든 작업이 끝났는지 확인하는 래치(done)
        val ready = CountDownLatch(concurrency)
        val start = CountDownLatch(1)
        val done = CountDownLatch(concurrency)

        // GIVEN: 성공/실패 카운트 및 에러 수집 구조
        val successCount = AtomicInteger(0)
        val failCount = AtomicInteger(0)
        val errors = ConcurrentLinkedQueue<String>()

        // GIVEN: 주문 요청 JSON을 미리 직렬화해 각 스레드에서 재사용한다.
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

        // GIVEN: 스레드 풀 크기를 동시성 수와 동일하게 맞춰 실제 동시성을 확보한다.
        val executor = Executors.newFixedThreadPool(concurrency)
        val startedAt = System.nanoTime()

        // WHEN: 동시 주문 요청을 스레드 풀에 제출한다.
        repeat(concurrency) {
            executor.submit {
                try {
                    // WHEN: 모든 스레드가 준비될 때까지 기다렸다가 동시에 시작한다.
                    ready.countDown()
                    start.await(10, TimeUnit.SECONDS)

                    // WHEN: 주문 API를 호출한다.
                    val response = mockMvc.perform(
                        post("/api/v1/order")
                            .with(authentication(testAuthentication))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson)
                    ).andReturn().response

                    // WHEN: 응답 상태에 따라 성공/실패를 집계한다.
                    if (response.status == 200) {
                        successCount.incrementAndGet()
                    } else {
                        failCount.incrementAndGet()
                        errors.add("HTTP_${response.status}")
                    }
                } catch (e: Exception) {
                    // WHEN: 예외 발생 시 실패로 집계하고 오류 유형을 기록한다.
                    failCount.incrementAndGet()
                    errors.add(e.javaClass.simpleName + ":" + (e.cause?.javaClass?.simpleName ?: "-"))
                } finally {
                    // WHEN: 각 스레드는 완료 시점에 done 래치를 감소시킨다.
                    done.countDown()
                }
            }
        }

        // WHEN: 모든 스레드가 준비되었는지 확인한 뒤 동시에 시작한다.
        assertTrue(ready.await(10, TimeUnit.SECONDS), "동시 요청 준비 단계에서 타임아웃 발생")
        start.countDown()
        // WHEN: 모든 스레드가 완료될 때까지 기다린다.
        assertTrue(done.await(180, TimeUnit.SECONDS), "동시 요청 처리 단계에서 타임아웃 발생")

        executor.shutdown()
        executor.awaitTermination(30, TimeUnit.SECONDS)

        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt)
        val errorSummary = errors.groupingBy { it }.eachCount().toString()

        // THEN: 실패 요청이 없어야 한다.
        assertEquals(0, failCount.get(), "실패 요청 존재. elapsedMs=$elapsedMs, errors=$errorSummary")
        // THEN: 성공 요청 수는 동시성 수와 동일해야 한다.
        assertEquals(concurrency, successCount.get(), "성공 요청 수가 기대값과 다름. elapsedMs=$elapsedMs")

        // THEN: 재고는 동시 주문 수만큼 정확히 차감되어야 한다.
        val finalStock = productRepository.findById(productId).orElseThrow().stock
        assertEquals(10000 - concurrency, finalStock, "최종 재고가 기대값과 다름")

        // THEN: 타임아웃/락 획득 실패 유형의 오류가 없어야 한다.
        val timeoutErrors = errors.filter {
            it.contains("Timeout", ignoreCase = true) ||
                it.contains("Connection is not available", ignoreCase = true) ||
                it.contains("CannotAcquireLock", ignoreCase = true)
        }
        assertTrue(timeoutErrors.isEmpty(), "타임아웃/락 획득 실패 계열 에러 발생: $timeoutErrors")
    }
}
