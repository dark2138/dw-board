package com.dwboard.dwboard.controller

import com.dwboard.dwboard.controller.dto.CommentCreateRequest
import com.dwboard.dwboard.controller.dto.CommentUpdateRequest
import com.dwboard.dwboard.controller.dto.toDto
import com.dwboard.dwboard.service.CommentService
import com.dwboard.dwboard.service.dto.CommentDetailResponseDto
import org.springframework.web.bind.annotation.*

@RestController
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long {
        return commentService.createComment(postId, commentCreateRequest.toDto(postId))
    }

    @GetMapping("/posts/{postId}/comments")
    fun getCommentsByPost(
        @PathVariable postId: Long,
    ): List<CommentDetailResponseDto> {
        return commentService.getCommentsByPost(postId)
    }

    @GetMapping("/comments/{commentId}")
    fun getComment(
        @PathVariable commentId: Long,
    ): CommentDetailResponseDto {
        return commentService.getComment(commentId)
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long {
        return commentService.updateComment(commentId, commentUpdateRequest.toDto())
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long {
        return commentService.deleteComment(commentId, deletedBy)
    }
}
