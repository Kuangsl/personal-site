package top.kuangsl.personal_site.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import top.kuangsl.personal_site.security.JwtAuthenticationFilter

/**
 * Spring Security 配置类
 * @author kuangsl
 * @date 2025/12/27
 * @email 690134117@qq.com
 * @param jwtAuthFilter JWT 过滤器
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter
) {

    /**
     * 密码加密器
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    /**
     * 认证管理器
     * @param config 认证配置
     * @return 认证管理器
     */
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    /**
     * 安全过滤链
     * @param http HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // 前后端分离禁用 CSRF
            .authorizeHttpRequests { auth ->
                // 放行登录和注册接口
                auth.requestMatchers("/api/auth/**").permitAll()
                auth.requestMatchers("/uploads/**").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
                // 其他所有接口都需要验证
                auth.anyRequest().authenticated()
            }
            // 禁用 Session (因为我们用 Token)
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            // 把我们的 JWT 过滤器加在用户名密码过滤器之前
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }



}
