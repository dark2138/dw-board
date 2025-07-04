package com.dwboard.dwboard.controller.dto

import com.dwboard.dwboard.service.dto.CommentCreateRequestDto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String
)

fun CommentCreateRequest.toDto(postId: Long) = CommentCreateRequestDto(
    content = content,
    createdBy = createdBy
)
