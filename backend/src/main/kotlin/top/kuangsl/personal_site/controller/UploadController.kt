package top.kuangsl.personal_site.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import top.kuangsl.personal_site.repository.UserRepository // 👈 引入 Repository
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@RestController
@RequestMapping("/api/upload")
class UploadController(private val userRepository: UserRepository) { // 👈 注入 userRepository

    private val uploadDir = "uploads"

    @PostMapping("/avatar")
    fun uploadAvatar(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        if (file.isEmpty) return ResponseEntity.badRequest().body("文件不能为空")

        try {
            // 1. 获取当前登录用户
            val auth = SecurityContextHolder.getContext().authentication
            val username = auth!!.name
            val user = userRepository.findByUsername(username)
                ?: return ResponseEntity.status(404).body("用户不存在")

            // 2. 🔥核心逻辑：检查并删除旧头像
            val oldAvatarUrl = user.avatarUrl
            if (!oldAvatarUrl.isNullOrBlank()) {
                // 假设数据库存的是 "/uploads/xxx.jpg"，我们需要转为本地路径 "uploads/xxx.jpg"
                // 去掉开头的 "/"
                val relativePath = if (oldAvatarUrl.startsWith("/")) oldAvatarUrl.substring(1) else oldAvatarUrl
                val oldFile = File(relativePath)

                // 只有当文件名包含 "uploads" 且文件存在时才删除 (防止误删系统文件)
                if (oldFile.path.contains("uploads") && oldFile.exists()) {
                    oldFile.delete()
                    println("已删除旧头像: ${oldFile.path}")
                }
            }

            // 3. 保存新文件
            val directory = File(uploadDir)
            if (!directory.exists()) directory.mkdirs()

            val suffix = file.originalFilename?.substringAfterLast(".", "jpg") ?: "jpg"
            val newFilename = "${UUID.randomUUID()}.$suffix"
            val filePath = Paths.get(uploadDir, newFilename)
            Files.write(filePath, file.bytes)

            // 4. 生成新 URL
            val newFileUrl = "/uploads/$newFilename"

            // 5. 🔥顺便直接更新数据库中的 avatarUrl (双重保险)
            user.avatarUrl = newFileUrl
            userRepository.save(user)

            return ResponseEntity.ok(newFileUrl)

        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.status(500).body("上传失败: ${e.message}")
        }
    }
}
