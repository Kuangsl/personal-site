package top.kuangsl.personal_site.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import top.kuangsl.personal_site.repository.UserRepository

// 更新配置文件请求
data class UpdateProfileRequest(
    val nickname: String?,
    val avatarUrl: String?,
    val email: String?
)

// 定义一个返回给前端的 VO (View Object)，避免暴露密码等敏感字段
data class UserInfoResponse(
    val id: Long?,
    val username: String,
    val nickname: String?,
    val avatarUrl: String?,
    val email: String?
)

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository) {

    @PostMapping("/update")
    fun updateProfile(@RequestBody request: UpdateProfileRequest): ResponseEntity<Any> {
        // 1. 获取当前登录用户名 (从 SecurityContext 获取，由 JwtFilter 填充)
        val authentication = SecurityContextHolder.getContext().authentication
        val currentUsername = authentication!!.name

        // 2. 查询数据库
        val user = userRepository.findByUsername(currentUsername)
            ?: return ResponseEntity.badRequest().body("用户不存在")

        // 3. 更新字段
        request.nickname?.let { user.nickname = it }
        request.avatarUrl?.let { user.avatarUrl = it }
        request.email?.let { user.email = it }

        // 4. 保存
        userRepository.save(user)

        return ResponseEntity.ok("更新成功")
    }
    // 获取当前用户信息
    @GetMapping("/info")
    fun getUserInfo(): ResponseEntity<Any> {
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth!!.name
        val user = userRepository.findByUsername(username)
            ?: return ResponseEntity.status(404).body("用户未找到")

        return ResponseEntity.ok(
            UserInfoResponse(
                id = user.id,
                username = user.username,
                nickname = user.nickname, // 数据库里叫 nickname
                avatarUrl = user.avatarUrl,
                email = user.email
            )
        )
    }
}
