package kr.co.myproject.dto.Comment;


import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kr.co.myproject.entity.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String author;
    private Comment parentComment;
    private String createdDate;

    public CommentDto(Comment comment, Comment parent, String nickname)
    {
        this.id = comment.getId();
        this.content = comment.getContent();

        this.author = nickname;

        this.parentComment = parent;

        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));;

    }
}
