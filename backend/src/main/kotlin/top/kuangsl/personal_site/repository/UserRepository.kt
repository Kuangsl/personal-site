package top.kuangsl.personal_site.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import top.kuangsl.personal_site.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // 用于 Security 根据用户名查询用户
    fun findByUsername(username: String): User?
}
