package com.dwboard.dwboard.repository

import com.dwboard.dwboard.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
