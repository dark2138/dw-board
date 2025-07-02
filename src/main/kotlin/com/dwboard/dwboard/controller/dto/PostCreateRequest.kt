package com.dwboard.dwboard.controller.dto

import com.dwboard.dwboard.service.dto.PostCreateRequestDto


data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequest.toDto() = PostCreateRequestDto(
    title = title,
    content = content,
    createdBy = createdBy
)
