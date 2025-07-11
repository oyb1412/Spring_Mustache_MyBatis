package kr.co.myproject.dto.User;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kr.co.myproject.enums.Role;
import kr.co.myproject.dto.Comment.CommentDto;
import kr.co.myproject.dto.Post.PostDto;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String nickname;
    private int level;
    private int exp;
    private int postCount;
    private int likedCount;
    private Role role;
    private boolean ban;
    private String createdDate;
    private String profileImageBase64;

    private int commentCount;
    private List<PostDto> posts = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();
    
    public UserDto(User user)
    {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.level = user.getLevel();
        this.exp = user.getExp();
        this.ban = user.isBan();
        this.role = user.getRole();
        this.profileImageBase64 = user.getProfileImageBase64();
        for (Post child : user.getPosts()) {
            this.posts.add(new PostDto(child));
        }
        for (Comment child : user.getComments()) {
            this.comments.add(new CommentDto(child, true));
        }
        this.commentCount = user.getComments().size();
        this.postCount = user.getPosts().size();
        this.likedCount = user.getLikedPostIds().size();
        this.createdDate = user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
