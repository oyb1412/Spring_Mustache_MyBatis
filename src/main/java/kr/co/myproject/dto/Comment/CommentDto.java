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
    private CommentDto parentCommentDto;
    private List<CommentDto> childCommentsDto = new ArrayList<>();
    private String createdDate;

    public CommentDto(Comment comment, boolean includeChildren)
    {
        this.id = comment.getId();
        this.content = comment.getContent();

        this.author = comment.getUser().getNickname();
        Comment parent = comment.getParentComment();

        this.parentCommentDto = parent != null ? new CommentDto(parent, false) : null;

        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));;

        if (includeChildren) {
        for (Comment child : comment.getChildComments()) {
            this.childCommentsDto.add(new CommentDto(child, true));
        }
    }
    }
}
