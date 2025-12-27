package top.kuangsl.personal_site.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // 获取 uploads 文件夹的绝对路径
        val uploadPath = File("uploads").absolutePath

        // 映射 URL "/uploads/**" 到本地文件系统
        // 注意：Windows下路径需要 "file:/" 前缀
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:$uploadPath/")
    }
}
