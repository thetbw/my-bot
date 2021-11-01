plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.6.6"
}

dependencies {
    implementation("io.ktor:ktor-client-serialization:1.5.1")
    implementation("com.charleskorn.kaml:kaml:0.36.0")
}

group = "xyz.thetbw"
version = "0.1.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}
