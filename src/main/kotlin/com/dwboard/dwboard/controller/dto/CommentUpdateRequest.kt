package com.dwboard.dwboard.controller.dto

import com.dwboard.dwboard.service.dto.CommentUpdateRequestDto

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String
)

fun CommentUpdateRequest.toDto() = CommentUpdateRequestDto(
    content = content,
    updatedBy = updatedBy
) 
