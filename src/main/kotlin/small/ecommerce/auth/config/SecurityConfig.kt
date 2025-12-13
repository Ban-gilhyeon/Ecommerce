package small.ecommerce.auth.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import small.ecommerce.auth.jwt.JwtAuthFilter
import small.ecommerce.auth.jwt.JwtTokenProvider

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtAuthFilter: JwtAuthFilter,
){
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                //공개 API
                it.requestMatchers(
                    "/",
                    "/api/v1/auth/**",
                    "/api/v1/brand/list",
                    "/api/v1/product/{brandId}/list"
                ).permitAll() // 회원가입 로그인

                //SELLER 전용
                it.requestMatchers(
                    "/api/v1/brand/add"
                ).hasRole("SELLER")

                //BUYER 전용?

                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .build()
    }
}