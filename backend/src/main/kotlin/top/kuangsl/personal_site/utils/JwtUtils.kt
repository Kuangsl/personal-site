package top.kuangsl.personal_site.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

/**
 * JWT 工具类
 * @author kuangsl
 * @date 2025/12/27
 * @email 690134117@qq.com
 * @param secretKeyString 密钥字符串
 * @param expirationTime 过期时间，单位：秒
 */
@Component
class JwtUtils(
    // 🔥 从 application.yml 读取值
    @param:Value("\${jwt.secret}") private val secretKeyString: String,
    @param:Value("\${jwt.expiration}") private val expirationTime: Long
) {

    // 这一步转换不能在构造函数直接做，因为注入可能发生在初始化之后
    // 但使用 Kotlin 的 lazy 委托是个很好的选择
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKeyString.toByteArray())
    }

    /**
     * 生成 Token
     * @param username 用户名
     * @return Token
     */
    fun generateToken(username: String): String {
        return Jwts.builder()
            .subject(username) // 将用户名放入 Token
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey) // 使用密钥签名
            .compact()
    }

    /**
     * 从 Token 中获取用户名
     * @param token 要解析的 Token
     * @return 用户名，如果解析失败返回 null
     */
    fun getUsernameFromToken(token: String): String? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (_: Exception) {
            null // 解析失败返回 null
        }
    }

    /**
     * 验证 Token 是否有效
     * @param token 要验证的 Token
     * @return true 表示有效，false 表示无效
     */
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (_: Exception) {
            false
        }
    }
}
