package com.study.newtil

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class NewTilApplication

fun main(args: Array<String>) {
	runApplication<NewTilApplication>(*args)
}
