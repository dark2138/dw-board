package com.dwboard.dwboard.service

import com.dwboard.dwboard.exception.CommentNotDeletableException
import com.dwboard.dwboard.exception.CommentNotFoundException
import com.dwboard.dwboard.exception.PostNotFoundException
import com.dwboard.dwboard.repository.CommentRepository
import com.dwboard.dwboard.repository.PostRepository
import com.dwboard.dwboard.service.dto.CommentCreateRequestDto
import com.dwboard.dwboard.service.dto.CommentUpdateRequestDto
import com.dwboard.dwboard.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createComment(postId: Long, createRequestDto: CommentCreateRequestDto): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException() as Throwable
        return commentRepository.save(createRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(id: Long, updateRequestDto: CommentUpdateRequestDto): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException() as Throwable
        comment.update(updateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(id: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        if (comment.createdBy != deletedBy) {
            throw CommentNotDeletableException() as Throwable
        }
        commentRepository.delete(comment)
        return id
    }
}
