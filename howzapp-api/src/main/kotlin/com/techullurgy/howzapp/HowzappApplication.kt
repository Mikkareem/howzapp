package com.techullurgy.howzapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HowzappApplication

fun main(args: Array<String>) {
	runApplication<HowzappApplication>(*args)
}