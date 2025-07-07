package com.dwboard.dwboard.service

import com.dwboard.dwboard.event.dto.LikeEvent
import com.dwboard.dwboard.repository.LikeRepository
import com.dwboard.dwboard.repository.PostRepository
import com.dwboard.dwboard.utill.RedisUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtil: RedisUtil,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun createLike(postId: Long, createdBy: String) {
        applicationEventPublisher.publishEvent(LikeEvent(postId, createdBy))
//       val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
//       redisUtil.increment(redisUtil.getLikeCountKey(postId))
//       return likeRepository.save(Like(post, createdBy)).id
    }

    // 게시글의 좋아요 개수를 조회한다. (Redis 캐시 우선, 없으면 DB 조회 후 캐싱)
    fun countLike(postId: Long): Long {
        // 1. Redis에 저장된 좋아요 개수 키 생성
        val key = redisUtil.getLikeCountKey(postId)
        // 2. Redis에서 좋아요 개수 조회 (캐시 히트 시 바로 반환)
        val cached = redisUtil.getCount(key)
        if (cached != null) return cached
        // 3. Redis에 값이 없으면 DB에서 좋아요 개수 조회
        val count = likeRepository.countByPostId(postId)
        // 4. 조회한 값을 Redis에 캐싱
        redisUtil.setData(key, count)
        // 5. DB에서 조회한 값을 반환
        return count
    }
}
