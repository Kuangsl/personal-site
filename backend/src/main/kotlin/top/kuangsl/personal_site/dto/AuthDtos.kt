package top.kuangsl.personal_site.dto

/**
 * 登录请求
 * @param username 登录用户名
 * @param password 登录密码
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * 身份验证响应
 * @param token 登录成功后返回的token
 * @param username 登录成功后返回的用户名
 */
data class AuthResponse(
    val token: String,
    val username: String
)

/**
 * 注册请求
 * @param username 注册用户名
 * @param password 注册密码
 */
data class RegisterRequest(
    val username: String,
    val password: String
)



