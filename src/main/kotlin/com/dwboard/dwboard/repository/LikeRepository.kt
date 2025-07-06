package com.dwboard.dwboard.repository

import com.dwboard.dwboard.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long> {
    fun countByPostId(postId: Long): Long
}
