package com.dwboard.dwboard.dto

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)
