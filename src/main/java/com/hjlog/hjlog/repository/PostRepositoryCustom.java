package com.hjlog.hjlog.repository;

import com.hjlog.hjlog.domain.Post;
import com.hjlog.hjlog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
