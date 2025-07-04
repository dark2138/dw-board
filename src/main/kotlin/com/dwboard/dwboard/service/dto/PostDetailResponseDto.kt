package com.dwboard.dwboard.service.dto

import com.dwboard.dwboard.domain.Post

data class PostDetailResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentDetailResponseDto>
)

fun Post.toDetailResponseDto() = PostDetailResponseDto(
    id = id,
    title = title,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString(),
    comments = comments.map {
        CommentDetailResponseDto(
            id = it.id,
            content = it.content,
            createdBy = it.createdBy,
            createdAt = it.createdAt.toString()
        )
    }
)
