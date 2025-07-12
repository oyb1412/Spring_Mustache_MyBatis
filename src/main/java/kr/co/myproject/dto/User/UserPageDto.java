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

    public UserPageDto(Long id, String title)
    {
        this.id = id;
        this.title = title;
    }
}
