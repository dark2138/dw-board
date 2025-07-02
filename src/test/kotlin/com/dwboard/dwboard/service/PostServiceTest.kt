package com.dwboard.dwboard.service

import com.dwboard.dwboard.domain.Post
import com.dwboard.dwboard.exception.PostNotDeletableException
import com.dwboard.dwboard.exception.PostNotFoundException
import com.dwboard.dwboard.exception.PostNotUpdatableException
import com.dwboard.dwboard.repository.PostRepository
import com.dwboard.dwboard.service.dto.PostCreateRequestDto
import com.dwboard.dwboard.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("게시글 생성 시") {
        When("게시글 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "sangwon"
                )
            )
            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "sangwon"
            }
        }
    }
    given("게시글 수정시") {
        val saved = postRepository.save(
            Post(title = "title", content = "content", createdBy = "sangwon")
        )
        When("정상 수정시") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "sangwon"
                )
            )
            then("게시글이 정상적으로 수정됨을 확인한다.") {
                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
            }
        }
        When("게시글이 없으면") {
            then("게시글을 찾을 수 없음을 확인한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "updated sangwon"
                        )
                    )
                }
            }
        }
        When("작성자가 동일하지 않으면") {
            then("게시글을 수정할 수 없음을 확인한다.") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "updated sangwon"
                        )
                    )
                }
            }
        }
    }

    given("게시글 삭제시") {
        val saved = postRepository.save(
            Post(title = "title", content = "content", createdBy = "sangwon")
        )
        When("정상 삭제시") {
            val postId = postService.deletePost(saved.id, "sangwon")
            then("게시글이 정상적으로 삭제됨을 확인한다.") {
                postId shouldBe saved.id
                val deleted = postRepository.findByIdOrNull(postId)
                deleted shouldBe null
            }
        }

        When("작성자가 동일하지 않으면") {
            val saved2 = postRepository.save(
                Post(title = "title", content = "content", createdBy = "sangwon")
            )
            then("게시글을 삭제할 수 없음을 확인한다.") {
                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved2.id, "sangwon2")
                }
            }
        }
    }
})
