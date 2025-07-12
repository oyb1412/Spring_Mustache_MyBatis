package kr.co.myproject.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    private Long id;

    private Long postId;

    private Long userId;

    private Long boardId;

    private Long parentCommentId;

    private String content;

    @Builder
    public Comment(Long postId, Long userId, Long boardId, String content, Long parentCommentId)
    {
        this.postId = postId;
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }
}
