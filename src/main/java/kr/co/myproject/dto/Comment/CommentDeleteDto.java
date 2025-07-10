package kr.co.myproject.dto.Comment;

import lombok.Getter;

@Getter
public class CommentDeleteDto {
    private Long id;
    private Long postId;
    private String type;
}
