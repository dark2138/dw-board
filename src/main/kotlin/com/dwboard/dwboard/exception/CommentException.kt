package com.dwboard.dwboard.exception

open class CommentException(message: String) : RuntimeException(message)

class CommentNotFoundException() : CommentException("댓글을 찾을 수 없습니다.")

class CommentNotUpdatableException() : CommentException("댓글을 수정할 수 없습니다.")

class CommentNotDeletableException() : CommentException("댓글을 삭제할 수 없습니다.")
