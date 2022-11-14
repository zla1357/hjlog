package com.hjlog.hjlog.request;

import com.hjlog.hjlog.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@ToString
@Getter
public class PostCreate {

    @NotBlank(message = "title을 입력해주세요.")
    private String title;

    @NotBlank(message = "content를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}
