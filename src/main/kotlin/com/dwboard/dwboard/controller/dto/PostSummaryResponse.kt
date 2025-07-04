package com.dwboard.dwboard.controller.dto

import com.dwboard.dwboard.service.dto.PostSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
)

fun Page<PostSummaryResponseDto>.toResponse() = PageImpl( // PageImpl은 페이징된 결과를 담는 실제 객체
    content.map { it.toResponse() }, // 실제 데이터(List<Post>)
    pageable, // 페이지 정보(PageRequest 등)
    totalElements // 전체 데이터 개수
)

fun PostSummaryResponseDto.toResponse() = PostSummaryResponse(
    id = id,
    title = title,
    createdBy = createdBy,
    createdAt = createdAt,
)

/*

 PageImpl이 뭐냐?
PageImpl은 Spring Data에서 제공하는 Page<T> 인터페이스의 기본 구현체야.
즉, 페이징된 결과(목록, 페이지 정보 등)를 담는 실제 객체.
보통 DB에서 페이징 쿼리 결과를 반환할 때, PageImpl로 감싸서 반환함.

val page: Page<Post> = PageImpl(
    content,    // 실제 데이터(List<Post>)
    pageable,   // 페이지 정보(PageRequest 등)
    totalCount  // 전체 데이터 개수
)



*/
