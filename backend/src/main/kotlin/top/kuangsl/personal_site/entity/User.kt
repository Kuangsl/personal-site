package top.kuangsl.personal_site.entity

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 使用 @Entity 告诉 Spring 这对应数据库里的一张表
 */
@Entity
@Table(name = "sys_user") // 数据库表名为 sys_user
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 50)
    var username: String,

    @Column(nullable = false)
    var password: String, // 实际开发中这里存的是加密后的哈希值

    @Column(length = 100)
    var email: String? = null,

    @Column(name = "create_time")
    var createTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "last_login")
    var lastLogin: LocalDateTime? = null,

    var role: String = "USER" // 权限角色：ADMIN, USER
)
