package com.example.MVCSpring6

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
class MvcSpring6Application

fun main(args: Array<String>) {
	runApplication<MvcSpring6Application>(*args)
}


