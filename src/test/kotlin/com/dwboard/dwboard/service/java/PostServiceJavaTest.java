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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceJavaTest {
    // PostRepository를 Mock으로 생성 (DB 접근 없이 동작 검증)
    @Mock
    private PostRepository postRepository;

    // PostService에 Mock 객체를 주입
    @InjectMocks
    private PostService postService;

    // 각 테스트 실행 전 Mock 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 게시글 생성 성공 테스트
    @Test
    void createPost_success() {
        PostCreateRequestDto dto = new PostCreateRequestDto("제목", "내용", "sangwon");
        Post saved = new Post("제목", "내용", "sangwon");
        saved.setId(1L);
        // 저장 동작을 Mock으로 지정
        when(postRepository.save(any(Post.class))).thenReturn(saved);
/*
when(...).thenReturn(...)
→ “...이 호출되면 ...을 반환해라”라는 의미
→ 즉, 특정 메서드가 호출될 때 원하는 값을 리턴하도록 미리 약속하는 것
any(Post.class)
→ “Post 타입의 어떤 객체가 들어와도 상관없다”는 의미
→ 즉, save 메서드에 어떤 Post 객체가 들어와도 아래 thenReturn에 지정한 값을 반환
saved
→ 테스트에서 미리 만들어둔 Post 객체(예: id가 1L로 세팅된 객체)

 */


        Long postId = postService.createPost(dto);
        assertTrue(postId > 0); // 생성된 ID가 0보다 큰지 확인
        verify(postRepository).save(any(Post.class)); // save가 호출됐는지 검증
// verify는 특정 메서드가 호출됐는지 검증하는 메서드
// 즉, postRepository.save(any(Post.class))가 호출됐는지 검증

    }

    // 게시글 수정 성공 테스트
    @Test
    void updatePost_success() {
        Post saved = new Post("title", "content", "sangwon");
        saved.setId(1L);
        // findById, save 동작을 Mock으로 지정
        when(postRepository.findById(1L)).thenReturn(Optional.of(saved));
        when(postRepository.save(any(Post.class))).thenReturn(saved);
        PostUpdateRequestDto dto = new PostUpdateRequestDto("update title", "update content", "sangwon");
        Long updatedId = postService.updatePost(1L, dto);
        assertEquals(saved.getId(), updatedId); // ID 일치 확인
        assertEquals("update title", saved.getTitle()); // 제목 변경 확인
        assertEquals("update content", saved.getContent()); // 내용 변경 확인
    }

    // 게시글 수정 시 게시글이 없는 경우 예외 테스트
    @Test
    void updatePost_notFound() {
        when(postRepository.findById(9999L)).thenReturn(Optional.empty());
        PostUpdateRequestDto dto = new PostUpdateRequestDto("update title", "update content", "updated sangwon");
        // PostNotFoundException 발생 여부 확인
        assertThrows(PostNotFoundException.class, () -> postService.updatePost(9999L, dto));
    }

    // 게시글 수정 시 작성자가 다를 때 예외 테스트
    @Test
    void updatePost_notUpdatable() {
        Post saved = new Post("title", "content", "someoneelse");
        saved.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(saved));
        PostUpdateRequestDto dto = new PostUpdateRequestDto("update title", "update content", "updated sangwon");
        // PostNotUpdatableException 발생 여부 확인
        assertThrows(PostNotUpdatableException.class, () -> postService.updatePost(1L, dto));
    }

    // 게시글 삭제 성공 테스트
    @Test
    void deletePost_success() {
        Post saved = new Post("title", "content", "sangwon");
        saved.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(saved));
        doNothing().when(postRepository).delete(saved); // delete 동작 Mock
        Long deletedId = postService.deletePost(1L, "sangwon");
        assertEquals(saved.getId(), deletedId); // 삭제된 ID 일치 확인
        verify(postRepository).delete(saved); // delete 호출 검증
    }

    // 게시글 삭제 시 작성자가 다를 때 예외 테스트
    @Test
    void deletePost_notDeletable() {
        Post saved = new Post("title", "content", "sangwon");
        saved.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(saved));
        // PostNotDeletableException 발생 여부 확인
        assertThrows(PostNotDeletableException.class, () -> postService.deletePost(1L, "otheruser"));
    }

    // 게시글 상세 조회 성공 테스트
    @Test
    void getPost_success() {
        Post saved = new Post("title", "content", "harris");
        saved.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(saved));
        var post = postService.getPost(1L);
        assertEquals(saved.getId(), post.getId()); // ID 일치
        assertEquals("title", post.getTitle()); // 제목 일치
        assertEquals("content", post.getContent()); // 내용 일치
        assertEquals("harris", post.getCreatedBy()); // 작성자 일치
    }

    // 게시글 상세 조회 시 없는 경우 예외 테스트
    @Test
    void getPost_notFound() {
        when(postRepository.findById(9999L)).thenReturn(Optional.empty());
        // PostNotFoundException 발생 여부 확인
        assertThrows(PostNotFoundException.class, () -> postService.getPost(9999L));
    }

    // 게시글 목록 조회(페이지네이션) 성공 테스트
    @Test
    void findPageBy_success() {
        Post post1 = new Post("title1", "content1", "harris1");
        post1.setId(1L);
        Post post2 = new Post("title2", "content2", "harris2");
        post2.setId(2L);
        // Page 객체를 Mock으로 생성
        Page<Post> page = new PageImpl<>(Arrays.asList(post1, post2), PageRequest.of(0, 5), 2);
        when(postRepository.findAll(any(), any(PageRequest.class))).thenReturn(page);
        var postPage = postService.findPageBy(PageRequest.of(0, 5), new PostSearchRequestDto());
        assertEquals(0, postPage.getNumber()); // 페이지 번호 확인
        assertEquals(5, postPage.getSize()); // 페이지 크기 확인
        assertEquals(2, postPage.getContent().size()); // 반환된 게시글 수 확인
        assertEquals("title1", postPage.getContent().get(0).getTitle()); // 첫 게시글 제목 확인
        assertEquals("harris1", postPage.getContent().get(0).getCreatedBy()); // 첫 게시글 작성자 확인
    }
}
