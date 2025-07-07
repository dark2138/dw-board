package com.dwboard.dwboard.event.dto

data class LikeEvent(
    val postId: Long,
    val createdBy: String,
)
