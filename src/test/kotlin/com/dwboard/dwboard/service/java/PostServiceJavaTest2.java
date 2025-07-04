package com.dwboard.dwboard.service.java;

import com.dwboard.dwboard.domain.Post;
import com.dwboard.dwboard.exception.PostNotDeletableException;
import com.dwboard.dwboard.exception.PostNotFoundException;
import com.dwboard.dwboard.exception.PostNotUpdatableException;
import com.dwboard.dwboard.repository.PostRepository;
import com.dwboard.dwboard.service.PostService;
import com.dwboard.dwboard.service.dto.PostCreateRequestDto;
import com.dwboard.dwboard.service.dto.PostUpdateRequestDto;
import com.dwboard.dwboard.service.dto.PostSearchRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("PostService 실무 패턴 테스트")
class PostServiceJavaTest2 {
    @Mock // DB 접근 없이 동작 검증을 위한 Mock 객체
    private PostRepository postRepository;
    @InjectMocks // Mock 객체가 주입된 PostService 인스턴스 생성
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 각 테스트 실행 전 Mock 초기화
    }

    // Fixture: Post 빌더 (테스트 데이터 생성 반복 줄임)
    private Post buildPost(Long id, String title, String content, String createdBy) {
        Post post = new Post(title, content, createdBy);
        post.setId(id);
        return post;
    }

    // Custom Assertion (반복되는 검증 로직 분리)
    private void assertPostEquals(Post expected, Post actual) {
        assertThat(actual.getId()) // assertj: actual.getId() 값이
            .isEqualTo(expected.getId()); // expected.getId()와 같은지 검증
        assertThat(actual.getTitle()) // assertj: actual.getTitle() 값이
            .isEqualTo(expected.getTitle()); // expected.getTitle()과 같은지 검증
        assertThat(actual.getContent()) // assertj: actual.getContent() 값이
            .isEqualTo(expected.getContent()); // expected.getContent()과 같은지 검증
        assertThat(actual.getCreatedBy()) // assertj: actual.getCreatedBy() 값이
            .isEqualTo(expected.getCreatedBy()); // expected.getCreatedBy()와 같은지 검증
    }

    @Nested // 테스트 그룹화(기능별로 묶어서 가독성↑)
    @DisplayName("게시글 생성")
    class CreatePost {
        @Test
        @DisplayName("정상적으로 게시글을 생성한다 [Given-When-Then]")
        void createPost_success() {
            // Given (준비)
            PostCreateRequestDto dto = new PostCreateRequestDto("제목", "내용", "sangwon");
            Post saved = buildPost(1L, "제목", "내용", "sangwon");
            given(postRepository.save(any(Post.class))).willReturn(saved);
            // When (실행)
            Long postId = postService.createPost(dto);
            // Then (검증)
            assertThat(postId) // assertj: postId 값이
                .isEqualTo(1L); // 1L과 같은지 검증
            then(postRepository).should().save(any(Post.class));
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {
        @Test
        @DisplayName("작성자가 맞으면 게시글을 수정한다") // 테스트 메서드 설명
        void updatePost_success() {
            // Given
            Post saved = buildPost(1L, "title", "content", "sangwon");
            given(postRepository.findById(1L)).willReturn(Optional.of(saved));
            given(postRepository.save(any(Post.class))).willReturn(saved);
            PostUpdateRequestDto dto = new PostUpdateRequestDto("update title", "update content", "sangwon");
            // When
            Long updatedId = postService.updatePost(1L, dto);
            // Then
            assertThat(updatedId) // assertj: updatedId 값이
                .isEqualTo(1L); // 1L과 같은지 검증
            assertThat(saved.getTitle()) // assertj: saved.getTitle() 값이
                .isEqualTo("update title"); // "update title"과 같은지 검증
            assertThat(saved.getContent()) // assertj: saved.getContent() 값이
                .isEqualTo("update content"); // "update content"과 같은지 검증
        }

        @ParameterizedTest(name = "작성자가 다르면 예외 발생: {2}") // 테스트 메서드 설명
        @CsvSource({ // 테스트 데이터 설명
            "1, title, someoneelse",
            "2, another, notowner"
        })
        @DisplayName("작성자가 다르면 PostNotUpdatableException 발생")
        void updatePost_notUpdatable(Long id, String title, String createdBy) {
            // Given
            Post saved = buildPost(id, title, "content", createdBy);
            given(postRepository.findById(id)).willReturn(Optional.of(saved));
            PostUpdateRequestDto dto = new PostUpdateRequestDto("update title", "update content", "otheruser");
            // When & Then
            assertThatThrownBy(() -> postService.updatePost(id, dto)) // assertj: 해당 람다 실행 시
                .isInstanceOf(PostNotUpdatableException.class); // PostNotUpdatableException이 발생하는지 검증

                /*
                 * () -> postService.updatePost(id, dto)
이것은 람다식(Lambda Expression)이야.
상세 설명
() -> postService.updatePost(id, dto)
파라미터가 없는 함수를 의미함 (())
함수의 본문은 postService.updatePost(id, dto)를 실행
즉, "이 람다를 실행하면 postService.updatePost(id, dto)가 호출된다"는 뜻
assertThatThrownBy(...)
이 메서드는 "이 람다를 실행했을 때 예외가 발생하는지"를 검증함
내부적으로 try-catch로 감싸서, 예외가 실제로 발생하는지 체크
.isInstanceOf(PostNotUpdatableException.class)
발생한 예외가 PostNotUpdatableException 타입인지 검증
왜 이렇게 쓰는가?
예외 발생 테스트에서,
"이 코드를 실행했을 때 특정 예외가 발생해야 한다"는 것을 명확하게 표현할 수 있음
람다식으로 감싸지 않으면,
예외가 테스트 메서드 실행 시점에 바로 터져서 테스트가 실패함
→ 람다로 감싸서 "실행 시 예외 발생"을 검증
한 줄 요약
() -> postService.updatePost(id, dto)는
"postService.updatePost(id, dto)를 실행하는 함수(코드 블록)"을 의미하고,
assertThatThrownBy는 이 함수 실행 시 예외가 발생하는지 체크하는 용도임.

new Runnable() {
    @Override
    public void run() {
        postService.updatePost(id, dto);
    }
}
                 *
                 *
                 *
                 */
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost {
        @Test
        @DisplayName("작성자가 맞으면 게시글을 삭제한다")
        void deletePost_success() {
            // Given
            Post saved = buildPost(1L, "title", "content", "sangwon");
            given(postRepository.findById(1L)).willReturn(Optional.of(saved)); // willReturn()은 메서드 호출 검증을 위한 메서드
            willDoNothing().given(postRepository).delete(saved); // willDoNothing()은 메서드 호출 검증을 위한 메서드
            // When
            Long deletedId = postService.deletePost(1L, "sangwon");
            // Then
            assertThat(deletedId) // assertj: deletedId 값이
                .isEqualTo(1L); // 1L과 같은지 검증
            then(postRepository).should().delete(saved); // delete 호출 검증
        }

        @Test
        @DisplayName("작성자가 다르면 PostNotDeletableException 발생")
        void deletePost_notDeletable() {
            // Given
            Post saved = buildPost(1L, "title", "content", "sangwon");
            given(postRepository.findById(1L)).willReturn(Optional.of(saved));
            // When & Then
            assertThatThrownBy(() -> postService.deletePost(1L, "otheruser")) // assertj: 해당 람다 실행 시
                .isInstanceOf(PostNotDeletableException.class); // PostNotDeletableException이 발생하는지 검증
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회")
    class GetPost {
        @Test
        @DisplayName("존재하는 게시글을 정상적으로 조회한다")
        void getPost_success() {
            // Given
            Post saved = buildPost(1L, "title", "content", "harris");
            given(postRepository.findById(1L)).willReturn(Optional.of(saved));
            // When
            var post = postService.getPost(1L);
            // Then
            assertThat(post.getId()) // assertj: post.getId() 값이
                .isEqualTo(1L); // 1L과 같은지 검증
            assertThat(post.getTitle()) // assertj: post.getTitle() 값이
                .isEqualTo("title"); // "title"과 같은지 검증
            assertThat(post.getContent()) // assertj: post.getContent() 값이
                .isEqualTo("content"); // "content"와 같은지 검증
            assertThat(post.getCreatedBy()) // assertj: post.getCreatedBy() 값이
                .isEqualTo("harris"); // "harris"와 같은지 검증
        }

        @Test
        @DisplayName("없는 게시글 조회 시 PostNotFoundException 발생")
        void getPost_notFound() {
            // Given
            given(postRepository.findById(9999L)).willReturn(Optional.empty());
            // When & Then
            assertThatThrownBy(() -> postService.getPost(9999L)) // assertj: 해당 람다 실행 시
                .isInstanceOf(PostNotFoundException.class); // PostNotFoundException이 발생하는지 검증
        }

        @Test
        @DisplayName("댓글 추가시")
        void getPost_withComments() {
            // Given
            Post saved = buildPost(1L, "title", "content", "harris");
            given(postRepository.findById(1L)).willReturn(Optional.of(saved)); // 1. 게시글 조회
            Comment comment1 = new Comment("comment1", "harris", saved); // 2. 댓글 생성
            Comment comment2 = new Comment("comment2", "harris", saved);
            Comment comment3 = new Comment("comment3", "harris", saved);
            given(commentRepository.findByPostId(1L)).willReturn(Arrays.asList(comment1, comment2, comment3)); // 3. 댓글 조회

            // When
            var post = postService.getPost(1L);

            // Then
            assertThat(post.getComments()) // assertj: post.getComments() 리스트가
                .hasSize(3); // 크기가 3인지 검증
            assertThat(post.getComments().get(0).getContent()) // assertj: 첫 댓글의 내용이
                .isEqualTo("comment1"); // "comment1"과 같은지 검증
            assertThat(post.getComments().get(1).getContent()) // assertj: 두 번째 댓글의 내용이
                .isEqualTo("comment2"); // "comment2"과 같은지 검증
            assertThat(post.getComments().get(2).getContent()) // assertj: 세 번째 댓글의 내용이
                .isEqualTo("comment3"); // "comment3"과 같은지 검증
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회")
    class FindPageBy {
        @Test
        @DisplayName("페이지네이션으로 게시글 목록을 조회한다")
        void findPageBy_success() {
            // Given
            Post post1 = buildPost(1L, "title1", "content1", "harris1");
            Post post2 = buildPost(2L, "title2", "content2", "harris2");
            Page<Post> page = new PageImpl<>(Arrays.asList(post1, post2), PageRequest.of(0, 5), 2);
            given(postRepository.findAll(any(), any(PageRequest.class))).willReturn(page);
            // When
            var postPage = postService.findPageBy(PageRequest.of(0, 5), new PostSearchRequestDto());
            // Then
            assertThat(postPage.getNumber()) // assertj: postPage.getNumber() 값이
                .isEqualTo(0); // 0과 같은지 검증
            assertThat(postPage.getSize()) // assertj: postPage.getSize() 값이
                .isEqualTo(5); // 5와 같은지 검증
            assertThat(postPage.getContent()) // assertj: postPage.getContent() 리스트가
                .hasSize(2); // 크기가 2인지 검증
            assertThat(postPage.getContent().get(0).getTitle()) // assertj: 첫 게시글의 제목이
                .isEqualTo("title1"); // "title1"과 같은지 검증
            assertThat(postPage.getContent().get(0).getCreatedBy()) // assertj: 첫 게시글의 작성자가
                .isEqualTo("harris1"); // "harris1"과 같은지 검증
        }
    }
}
