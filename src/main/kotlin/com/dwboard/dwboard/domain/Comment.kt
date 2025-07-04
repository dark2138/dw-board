package com.dwboard.dwboard.domain

import jakarta.persistence.*

@Entity
class Comment(
    var content: String,
    createdBy: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun update(content: String, updatedBy: String) {
        this.content = content
        super.updatedBy(updatedBy)
    }
} 
