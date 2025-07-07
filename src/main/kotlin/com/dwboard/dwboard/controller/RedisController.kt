package com.dwboard.dwboard.controller

import com.dwboard.dwboard.utill.RedisUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RedisController(
    private val redisUtil: RedisUtil,
) {
    @GetMapping("/redis")
    fun getRedisCount(): Long {
        redisUtil.increment("count")
        return redisUtil.getCount("count") ?: 0L
    }
}
