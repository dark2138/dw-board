package com.dwboard.dwboard.service.java;

import com.dwboard.dwboard.exception.CommentNotDeletableException;
import com.dwboard.dwboard.exception.CommentNotUpdatableException;
import com.dwboard.dwboard.exception.PostNotFoundException;
import com.dwboard.dwboard.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.dwboard.dwboard.domain.Comment;
import com.dwboard.dwboard.domain.Post;
import com.dwboard.dwboard.repository.CommentRepository;
import com.dwboard.dwboard.repository.PostRepository;
import com.dwboard.dwboard.service.dto.CommentCreateRequestDto;
import com.dwboard.dwboard.service.dto.CommentUpdateRequestDto;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;



@DisplayName("CommentService 실무 패턴 테스트")
public class CommentServiceJavaTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   @Nested
   @DisplayName("댓글 생성시")
   class CreateComment {
    Post post = postRepository.save(new Post("제목", "내용", "harris"));
    @Test
    @DisplayName("정상적인 요청이면")
    void createComment_success() {
        // Given
        Comment comment = commentRepository.save(new Comment("content", "harris", post));

        // When
        Long commentId = commentService.createComment(post.getId(), new CommentCreateRequestDto("content", "harris", post.getId()));

        // Then
        assertThat(commentId) // assertj: commentId 값이
            .isEqualTo(1L); // 1L과 같은지 검증
        then(commentRepository).should().save(any(Comment.class));
    }


    @Test
    @DisplayName("게시글이 존재하지 않으면")
    void createComment_notFound() {
        // Given
        CommentCreateRequestDto dto = new CommentCreateRequestDto("content", "harris", 9999L);

        // When & Then
        assertThatThrownBy(() -> commentService.createComment(9999L, dto))
            .isInstanceOf(PostNotFoundException.class);
        }
   }

   @Nested
   @DisplayName("댓글 수정시")
   class UpdateComment {
    Post post = postRepository.save(new Post("title", "content", "harris"));
    Comment comment = commentRepository.save(new Comment("content", "harris", post));
    @Test
    @DisplayName("정상적인 요청이면")
    void updateComment_success() {

        // Given
        Comment comment = commentRepository.save(new Comment("수정된 내용", "harris", post));

        // When
        Long updatedId = commentService.updateComment(comment.getId(), new CommentUpdateRequestDto("수정된 내용", "harris", comment.getId()));

        // Then
        assertThat(updatedId) // assertj: updatedId 값이
            .isEqualTo(comment.getId()); // comment.getId()와 같은지 검증
        assertThat(comment.getContent()) // assertj: comment.getContent() 값이
            .isEqualTo("수정된 내용"); // "update content"과 같은지 검증
        then(commentRepository).should().save(any(Comment.class));

    }

    @Test
    @DisplayName("작성자가 다르면")
    void updateComment_notUpdatable() {
        // Given
        Comment comment = commentRepository.save(new Comment("content", "someoneelse", post));
        // When & Then
        assertThatThrownBy(() -> commentService.updateComment(comment.getId(), new CommentUpdateRequestDto("수정된 내용", "harris2", comment.getId())))
            .isInstanceOf(CommentNotUpdatableException.class);
    }
   }

   @Nested
   @DisplayName("댓글 삭제시")
   class DeleteComment {
    Post post = postRepository.save(new Post("title", "content", "harris"));
    Comment comment = commentRepository.save(new Comment("content", "harris", post));
    @Test
    @DisplayName("정상적인 요청이면")
    void deleteComment_success() {
        // Given
        Comment comment = commentRepository.save(new Comment("content", "harris", post));
        // When
        Long deletedId = commentService.deleteComment(comment.getId(), "harris");
        // Then
        assertThat(deletedId) // assertj: deletedId 값이
            .isEqualTo(comment.getId()); // comment.getId()와 같은지 검증
        then(commentRepository).should().delete(comment);
    }

    @Test
    @DisplayName("작성자가 다르면")
    void deleteComment_notDeletable() {
        // Given
        Comment comment = commentRepository.save(new Comment("content", "someoneelse", post));
        // When & Then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), "harris2"))
            .isInstanceOf(CommentNotDeletableException.class);
    }
   }


}




