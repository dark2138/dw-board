package com.dwboard.dwboard.domain

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass // 상속받은 클래스에서 필드를 상속받음
abstract class BaseEntity(
    createdBy: String,
) {
    val createdBy: String = createdBy
    val createdAt: LocalDateTime = LocalDateTime.now()

    var updatedBy: String? = null // ? 는 null 값을 허용하는 것
        protected set // class 외부에서 접근 불가
    var updatedAt: LocalDateTime? = null
        protected set // class 외부에서 접근 불가

    fun update(updatedBy: String) {
        this.updatedBy = updatedBy
        this.updatedAt = LocalDateTime.now()
    }
}
