package com.dwboard.dwboard.event

import com.dwboard.dwboard.domain.Like
import com.dwboard.dwboard.event.dto.LikeEvent
import com.dwboard.dwboard.exception.PostNotFoundException
import com.dwboard.dwboard.repository.LikeRepository
import com.dwboard.dwboard.repository.PostRepository
import com.dwboard.dwboard.utill.RedisUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class LikeEventHandler(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val redisUtil: RedisUtil,
) {

    @Async // 비동기 처리를 위한 어노테이션
    @TransactionalEventListener(LikeEvent::class)
    fun handle(event: LikeEvent) {
        // 1. (예시) 비동기 지연 시뮬레이션 (실제 운영에서는 불필요)
        Thread.sleep(3000)
// 2. postId로 게시글 조회, 없으면 예외 발생
        val post = postRepository.findByIdOrNull(event.postId) ?: throw PostNotFoundException() as Throwable
        // 3. Redis에 좋아요 수를 먼저 증가시킴 (빠른 응답, 캐시 우선)
        redisUtil.increment(redisUtil.getLikeCountKey(event.postId))
        // 4. DB에 Like 엔티티 저장 (실제 데이터 영속화)
        likeRepository.save(Like(post, event.createdBy))
    }
}
