package com.dwboard.dwboard.dto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)
