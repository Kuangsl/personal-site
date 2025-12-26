pluginManagement {
    repositories {
        gradlePluginPortal()  // 官方快
        mavenCentral()        // 备
        maven { name = "aliyun-plugin"; url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()  // 1st: 稳定快
        maven { name = "aliyun-public"; url = uri("https://maven.aliyun.com/repository/public") }
        google()
        maven { name = "aliyun-google"; url = uri("https://maven.aliyun.com/repository/google") }
    }
}

rootProject.name = "personal-site"
