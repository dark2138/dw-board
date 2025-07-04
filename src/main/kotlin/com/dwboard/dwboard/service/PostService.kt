package com.dwboard.dwboard.service

import com.dwboard.dwboard.exception.PostNotDeletableException
import com.dwboard.dwboard.exception.PostNotFoundException
import com.dwboard.dwboard.repository.PostRepository
import com.dwboard.dwboard.service.dto.PostCreateRequestDto
import com.dwboard.dwboard.service.dto.PostDetailResponseDto
import com.dwboard.dwboard.service.dto.PostSearchRequestDto
import com.dwboard.dwboard.service.dto.PostSummaryResponseDto
import com.dwboard.dwboard.service.dto.PostUpdateRequestDto
import com.dwboard.dwboard.service.dto.toDetailResponseDto
import com.dwboard.dwboard.service.dto.toEntity
import com.dwboard.dwboard.service.dto.toSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

    @Transactional
    fun updatePost(id: Long, requestDto: PostUpdateRequestDto): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        post.update(requestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, deleteBy: String): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        if (post.createdBy != deleteBy) {
            throw PostNotDeletableException()
        }
        postRepository.delete(post)
        return id
    }

    fun getPost(id: Long): PostDetailResponseDto {
        return postRepository.findByIdOrNull(id)?.toDetailResponseDto() ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PostSearchRequestDto): Page<PostSummaryResponseDto> {
        return postRepository.findPageBy(pageRequest, postSearchRequestDto)
            .toSummaryResponseDto() // 페이지 데이터를 결과 형식으로 변환
    }
}
