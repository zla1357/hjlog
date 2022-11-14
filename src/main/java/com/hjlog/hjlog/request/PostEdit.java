package com.hjlog.hjlog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@ToString
@Getter
public class PostEdit {

    @NotBlank(message = "title을 입력해주세요.")
    private String title;

    @NotBlank(message = "content를 입력해주세요.")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
