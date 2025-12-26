package top.kuangsl.personal_site.security
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import top.kuangsl.personal_site.service.CustomUserDetailsService
import top.kuangsl.personal_site.utils.JwtUtils

/**
 * JWT 认证过滤器类
 * @author KuangSL
 * @date 2025/12/27
 * @email 690134117@qq.com
 * @param userDetailsService 自定义用户详情服务
 * @param jwtUtils JWT 工具类
 */
@Component
class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val jwtUtils: JwtUtils // 注入刚才写的工具类
) : OncePerRequestFilter() {
    /**
     * 过滤器内部逻辑
     * @param request 请求对象
     * @param response 响应对象
     * @param filterChain 过滤链
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. 获取请求头
        val authHeader = request.getHeader("Authorization")

        // 2. 如果没有 Token 或者格式不对，直接放行（让 Security 后续处理）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // 3. 提取 Token
        val jwt = authHeader.substring(7)

        // 4. 解析用户名
        val username = jwtUtils.getUsernameFromToken(jwt)

        // 5. 如果用户名存在，且当前上下文没有认证过
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            // 6. 验证 Token 是否有效
            if (jwtUtils.validateToken(jwt)) {
                // 7. 加载用户详情
                val userDetails: UserDetails  = userDetailsService.loadUserByUsername(username)

                // 8. 创建认证 Token 并设置到 Security 上下文中
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                // 🔥 关键一步：告诉 Spring Security "这个人已经登录了"
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        // 9. 继续过滤链
        filterChain.doFilter(request, response)
    }
}
