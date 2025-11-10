import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.spring") version "2.2.0"
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.2.0"
}

group = "com.techullurgy"
version = "1.0"
description = "Social Media Application for Connecting People"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.google.cloud:google-cloud-storage:2.57.0")

    // Feign Client
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")


    // JWT
    val jwt = "0.13.0"
    implementation("io.jsonwebtoken:jjwt-api:$jwt")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwt")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwt")

    testImplementation("io.mockk:mockk:1.14.6")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-opt-in=kotlin.uuid.ExperimentalUuidApi", "-Xcontext-parameters")
    }
}