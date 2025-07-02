package com.dwboard.dwboard.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Post(
    title: String,
    content: String,
    createdBy: String,
) : BaseEntity(
    createdBy
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L // val이라 한 번 정해지면 바꿀 수 없음.

    var title: String = title // var이라 값을 바꿀 수 있음.
        protected set
    var content: String = content
        protected set
}
