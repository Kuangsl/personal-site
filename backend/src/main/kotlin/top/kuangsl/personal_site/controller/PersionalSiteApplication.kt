package top.kuangsl.personal_site.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// @RestController 告诉 Spring 这是一个处理 HTTP 请求的类，并返回 JSON/字符串
@RestController
@RequestMapping("/api/test")
class TestController {

    @GetMapping("/hello")
    fun sayHello(): String {
        // Kotlin 的函数体如果只有一行，可以直接写 =
        // 但为了清晰，我这里写标准形式
        return "Hello! Backend is running successfully. Current Status: Normal."
    }
}
