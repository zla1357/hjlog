package com.hjlog.hjlog.controller;

import com.hjlog.hjlog.exception.InvalidRequest;
import com.hjlog.hjlog.request.PostCreate;
import com.hjlog.hjlog.request.PostEdit;
import com.hjlog.hjlog.request.PostSearch;
import com.hjlog.hjlog.response.PostResponse;
import com.hjlog.hjlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        request.validate();
        
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
