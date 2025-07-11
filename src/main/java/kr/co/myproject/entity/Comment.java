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

    private Post post;

    private User user;

    private String content;

    private Comment parentComment;

    private List<Comment> childComments = new ArrayList<>();

    @Builder
    public Comment(Post post, User user, String content, Comment parentComment, List<Comment> childComments)
    {
        this.post = post;
        this.user = user;
        this.content = content;
        this.parentComment = parentComment;
        this.childComments = childComments;
    }

    public void SetChildComment(Comment comment)
    {
        childComments.add(comment);
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
