package com.dwboard.dwboard.service

import com.dwboard.dwboard.domain.Comment
import com.dwboard.dwboard.exception.*
import com.dwboard.dwboard.repository.CommentRepository
import com.dwboard.dwboard.repository.PostRepository
import com.dwboard.dwboard.service.dto.CommentCreateRequestDto
import com.dwboard.dwboard.service.dto.CommentDetailResponseDto
import com.dwboard.dwboard.service.dto.CommentUpdateRequestDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {
    @Transactional
    fun createComment(postId: Long, requestDto: CommentCreateRequestDto): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        val comment = Comment(
            content = requestDto.content,
            createdBy = requestDto.createdBy,
            post = post
        )
        return commentRepository.save(comment).id
    }

    fun getComment(id: Long): CommentDetailResponseDto {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        return CommentDetailResponseDto(
            id = comment.id,
            content = comment.content,
            createdBy = comment.createdBy,
            createdAt = comment.createdAt.toString()
        )
    }

    fun getCommentsByPost(postId: Long): List<CommentDetailResponseDto> {
        return commentRepository.findByPostId(postId).map {
            CommentDetailResponseDto(
                id = it.id,
                content = it.content,
                createdBy = it.createdBy,
                createdAt = it.createdAt.toString()
            )
        }
    }

    @Transactional
    fun updateComment(id: Long, requestDto: CommentUpdateRequestDto): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        if (comment.createdBy != requestDto.updatedBy) {
            throw CommentNotUpdatableException()
        }
        comment.update(requestDto.content, requestDto.updatedBy)
        return comment.id
    }

    @Transactional
    fun deleteComment(id: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        if (comment.createdBy != deletedBy) {
            throw CommentNotDeletableException()
        }
        commentRepository.delete(comment)
        return comment.id
    }
} 
