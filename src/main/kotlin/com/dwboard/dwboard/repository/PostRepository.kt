package com.dwboard.dwboard.repository

import com.dwboard.dwboard.domain.Post
import com.dwboard.dwboard.domain.QPost.post
import com.dwboard.dwboard.service.dto.PostSearchRequestDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface PostRepository : JpaRepository<Post, Long>, CustomPostRepository

interface CustomPostRepository {
    fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PostSearchRequestDto): Page<Post>
}

class CustomPostRepositoryImpl : CustomPostRepository, QuerydslRepositorySupport(Post::class.java) {
    override fun findPageBy(pageRequest: Pageable, postSearchRequestDto: PostSearchRequestDto): Page<Post> {
        // QueryDSL을 사용해 동적 검색 쿼리 생성
        val result = from(post) // QPost 객체(post) 기준으로 쿼리 시작
            .where(
                // title이 null이 아니면 'post.title.contains(title)' 조건 추가
                postSearchRequestDto.title?.let { post.title.contains(it) }, // let : null이 아닐 때만 블록을 실행하고, 결과를 반환
                // createdBy가 null이 아니면 'post.createdBy.eq(createdBy)' 조건 추가
                postSearchRequestDto.createdBy?.let { post.createdBy.eq(it) }
            )
            // 최신순 정렬
            .orderBy(post.createdAt.desc())
            // 페이징 처리: offset(시작 위치), limit(페이지 크기)
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            // 쿼리 실행 및 결과 반환
            .fetchResults()
        // 결과를 Spring Data의 Page 객체로 변환해서 반환
        return PageImpl(result.results, pageRequest, result.total)
    }
}
