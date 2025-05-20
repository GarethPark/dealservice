package com.example.dealservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class DealserviceApplication

fun main(args: Array<String>) {
	runApplication<DealserviceApplication>(*args)
}
