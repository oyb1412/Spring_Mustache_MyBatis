package kr.co.myproject.dto.Comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRegisterDto {

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    private Long postId;

    private Long parentCommentId;

    private String type;

    public Comment buildCommentEntity(Post post, User user, Comment parentComment)
    {
        return Comment.builder()
                      .content(content)
                      .postId(post.getId())
                      .userId(user.getId())
                      .parentCommentId(parentComment.getId())
                      .build();
    }

   
}
