package com.techullurgy.howzapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@EnableFeignClients
class HowzappApplication

fun main(args: Array<String>) {
	runApplication<HowzappApplication>(*args)
}