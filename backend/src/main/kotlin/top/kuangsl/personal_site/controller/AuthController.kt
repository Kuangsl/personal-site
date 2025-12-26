package top.kuangsl.personal_site.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import top.kuangsl.personal_site.dto.AuthResponse
import top.kuangsl.personal_site.dto.LoginRequest
import top.kuangsl.personal_site.dto.RegisterRequest
import top.kuangsl.personal_site.entity.User
import top.kuangsl.personal_site.repository.UserRepository
import top.kuangsl.personal_site.utils.JwtUtils

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {

    /**
     * 注册接口，返回注册成功或失败信息
     * @param request 注册请求
     * @return 注册成功或失败信息
     */
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<String> {
        // 1. 检查用户名是否存在
        val existingUser = userRepository.findByUsername(request.username)
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("错误：用户名已存在")
        }
        val password = request.password ?: return ResponseEntity.badRequest().body("错误：密码不能为空")
        val encodedPassword = passwordEncoder.encode(password)!!
        // 2. 使用 newUser 来创建新对象，它的类型是确定的 User（非空）
        val newUser = User(
            username = request.username,
            password = encodedPassword,
            role = "USER",
            status = 1
        )

        // 3. 保存到数据库
        userRepository.save(newUser)

        return ResponseEntity.ok("注册成功")
    }

    /**
     * 登录接口，返回 Token 给前端
     * @param request 登录请求
     * @return Token
     */
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            // 1. Spring Security 自动校验用户名和密码
            // 如果密码不对，这里会直接抛出异常，跳到 catch
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )

            // 2. 校验通过，生成 Token
            val token = jwtUtils.generateToken(request.username)

            // 3. 返回 Token 给前端
            ResponseEntity.ok(AuthResponse(token, request.username))

        } catch (e: Exception) {
            ResponseEntity.status(401).body("登录失败：用户名或密码错误")
        }
    }
}
