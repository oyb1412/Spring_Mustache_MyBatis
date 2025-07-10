package kr.co.myproject.dto.User;

import java.util.ArrayList;
import java.util.List;

import kr.co.myproject.dto.Comment.CommentDto;
import kr.co.myproject.entity.Comment;
import lombok.Getter;

@Getter
public class UserPageDto {
    private Long id;
    private String title;
    private List<CommentDto> comments = new ArrayList<>();

    public UserPageDto(Long id, String title, List<CommentDto> comments)
    {
        this.id = id;
        this.title = title;
        this.comments = comments;
    }
}
