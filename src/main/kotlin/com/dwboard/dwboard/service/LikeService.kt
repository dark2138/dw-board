package com.dwboard.dwboard.service

import com.dwboard.dwboard.domain.Like
import com.dwboard.dwboard.exception.PostNotFoundException
import com.dwboard.dwboard.repository.LikeRepository
import com.dwboard.dwboard.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createLike(postId: Long, createdBy: String) {
        val post = postRepository.findById(postId)
            .orElseThrow { PostNotFoundException() }
        likeRepository.save(Like(post, createdBy))
    }

    fun countLike(postId: Long): Long {
        return likeRepository.countByPostId(postId)
    }
}
