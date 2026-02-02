package small.ecommerce.auth.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import small.ecommerce.domain.auth.dto.UserPrincipal
import small.ecommerce.domain.user.UserRole
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
) {
    private val validity = 1000L * 60 * 60 // 1시간

    //Access 토큰 발급
    fun generateAccessToken(userId: Long, roles: UserRole):String{
        val now = Date()
        return Jwts.builder()
            .claim("userId", userId)
            .claim("role", roles.name)
            .issuedAt(now)
            .expiration(Date(now.time + validity))
            .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .compact()
    }

    //Access 토큰 --> userPrincipal
    fun getUserPrincipalFromAccessToken(token: String): UserPrincipal {
        val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        val userId = claims["userId"].toString().toLong()
        val userRole = UserRole.valueOf(claims["role"].toString())

        return UserPrincipal.from(userId,userRole)
    }


    fun validateAccessToken(token: String): Boolean =
        try {
            val key: SecretKey = Keys.hmacShaKeyFor(secretKey.toByteArray())

            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            claims.payload.expiration.after(Date())
        } catch (e: Exception) {
            false
        }
}
