package com.hjlog.hjlog.service;

import com.hjlog.hjlog.domain.Post;
import com.hjlog.hjlog.exception.PostNotFound;
import com.hjlog.hjlog.repository.PostRepository;
import com.hjlog.hjlog.request.PostCreate;
import com.hjlog.hjlog.request.PostEdit;
import com.hjlog.hjlog.request.PostSearch;
import com.hjlog.hjlog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }
    
    @Test
    @DisplayName("글 한개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> {
                            return Post.builder()
                                    .title("제목 - " + i)
                                    .content("내용 - " + i)
                                    .build();
                        })
                        .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(20, posts.size());
        assertEquals("제목 - 30", posts.get(0).getTitle());
        assertEquals("제목 - 26", posts.get(4).getTitle());
    }
    
    @Test
    @DisplayName("글 제목 수정")
    public void test4() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .title("foo2")
                .build();

        // when
        postService.edit(requestPost.getId(), postEdit);
        
        // then
        Post changedTitle = postRepository.findById(requestPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + requestPost.getId()));

        assertEquals("foo2", changedTitle.getTitle());
        assertEquals("bar", changedTitle.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    public void test5() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .content("bar2")
                .build();

        // when
        postService.edit(requestPost.getId(), postEdit);

        // then
        Post changedTitle = postRepository.findById(requestPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + requestPost.getId()));

        assertEquals("foo", changedTitle.getTitle());
        assertEquals("bar2", changedTitle.getContent());
    }
    
    @Test
    @DisplayName("게시글 삭제")
    public void test6() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        postService.delete(requestPost.getId());
        
        // then
        assertEquals(0, postRepository.count());
    }
    
    @Test
    @DisplayName("게시글 조회 실패 - 존재하지 않는 글")
    public void test7() {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 존재하지 않는 글")
    public void test8() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(requestPost.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 실패 - 존재하지 않는 글")
    public void test9() throws Exception {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .content("bar2")
                .build();

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(requestPost.getId() + 1L, postEdit);
        });
    }
}