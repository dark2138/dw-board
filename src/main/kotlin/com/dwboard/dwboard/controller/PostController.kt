package com.dwboard.dwboard.controller

import com.dwboard.dwboard.controller.dto.PostCreateRequest
import com.dwboard.dwboard.controller.dto.PostDetailResponse
import com.dwboard.dwboard.controller.dto.PostSearchRequest
import com.dwboard.dwboard.controller.dto.PostSummaryResponse
import com.dwboard.dwboard.controller.dto.PostUpdateRequest
import com.dwboard.dwboard.controller.dto.toDto
import com.dwboard.dwboard.controller.dto.toResponse
import com.dwboard.dwboard.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService,
) {

    @PostMapping("/posts")
    fun createPost(
        @RequestBody postCreateRequest: PostCreateRequest,
    ): Long {
        return postService.createPost(postCreateRequest.toDto())
    }

    @PutMapping("/posts/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest,
    ): Long {
        return postService.updatePost(id, postUpdateRequest.toDto())
    }

    @DeleteMapping("/posts/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long {
        return postService.deletePost(id, createdBy)
    }

    @GetMapping("/posts/{id}")
    fun getPost(
        @PathVariable id: Long,
    ): PostDetailResponse {
        return postService.getPost(id).toResponse()
    }

    @GetMapping("/posts")
    fun getPosts(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        return postService.findPageBy(pageable, postSearchRequest.toDto()) // dto로 변환
            .toResponse() // toResponse() 메서드를 호출하여 페이지 데이터를 변환
    }
}
