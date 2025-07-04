package com.dwboard.dwboard.repository

import com.dwboard.dwboard.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPostId(postId: Long): List<Comment>
} 
