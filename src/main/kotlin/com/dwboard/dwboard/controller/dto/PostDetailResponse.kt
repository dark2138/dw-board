package com.dwboard.dwboard.controller.dto

import com.dwboard.dwboard.service.dto.PostDetailResponseDto

data class CommentDetailResponse(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String
)

data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentDetailResponse>
)

fun PostDetailResponseDto.toResponse() = PostDetailResponse(
    id = id,
    title = title,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt,
    comments = comments.map {
        CommentDetailResponse(
            id = it.id,
            content = it.content,
            createdBy = it.createdBy,
            createdAt = it.createdAt
        )
    }
)
