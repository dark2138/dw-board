package com.dwboard.dwboard.service.dto

import com.dwboard.dwboard.domain.Comment
import com.dwboard.dwboard.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post) = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)
