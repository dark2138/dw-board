package com.dwboard.dwboard.service.dto

data class CommentDetailResponseDto(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String
)
