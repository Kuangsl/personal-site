package top.kuangsl.personal_site.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

/**
 * 用户实体类
 * @author kuangsl
 * @date 2025/12/26
 * @email 690134117@qq.com
 * @param id 用户id
 * @param username 用户名
 * @param password 密码
 * @param email 邮箱
 * @param nickname 昵称
 * @param avatarUrl 头像url
 * @param role 用户类型 ADMIN, USER
 * @param status 状态 1: 正常, 0: 禁用
 * @param createTime 创建时间
 * @param updateTime 更新时间
 */
@Entity
@Table(name = "sys_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false, length = 50)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(length = 100)
    var email: String? = null,

    @Column(name = "nickname", length = 50)//昵称
    var nickname: String? = null,

    @Column(name = "avatar_url")//头像url
    var avatarUrl: String? = null,

    @Column(nullable = false)
    var role: String = "USER",

    @Column(nullable = false)
    var status: Int = 1,

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    var createTime: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "update_time")
    var updateTime: LocalDateTime? = null,
)
