package com.dwboard.dwboard.domain

import com.dwboard.dwboard.exception.PostNotUpdatableException
import com.dwboard.dwboard.service.dto.PostUpdateRequestDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Post(
    var title: String,
    var content: String,
    createdBy: String, // 생성자
    @OneToMany(mappedBy = "post", cascade = [jakarta.persistence.CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun update(postUpdateRequestDto: PostUpdateRequestDto) {
        if (postUpdateRequestDto.updatedBy != this.createdBy) {
            throw PostNotUpdatableException()
        }
        this.title = postUpdateRequestDto.title
        this.content = postUpdateRequestDto.content
        super.updatedBy(postUpdateRequestDto.updatedBy)
    }
}
