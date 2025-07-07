package com.dwboard.dwboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync // 비동기 처리를 위한 어노테이션
@SpringBootApplication
class DwBoardApplication

fun main(args: Array<String>) {
    runApplication<DwBoardApplication>(*args)
}
