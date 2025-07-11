package kr.co.myproject.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kr.co.myproject.enums.FindPasswordQuestion;
import kr.co.myproject.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private int level = 1;

    private int exp = 0;

    private boolean ban = false;

    private String provider;

    private String providerId;

    private String profileImageBase64;

    private FindPasswordQuestion findPasswordQuestion;

    private String findPasswordQuestionAnswer;

    private Role role = Role.MEMBER; // DEFAULT값 지정

    private List<Post> posts = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private Set<Long> likedPostIds = new HashSet<>();

    @Builder
    public User(Long id, String username, String password, String nickname,
    String provider, String providerId,
    Role role, List<Post> posts, List<Comment> comments, 
    LocalDateTime createDate, LocalDateTime modifyDate, FindPasswordQuestion findPasswordQuestion, String findPasswordQuestionAnswer)
    {

        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.exp = 0;
        this.ban = false;
        this.level = 1;
        this.role = role;
        this.posts = posts;
        this.comments = comments;
        this.findPasswordQuestion = findPasswordQuestion;
        this.findPasswordQuestionAnswer = findPasswordQuestionAnswer;
        this.createdDate = createDate;
        this.modifiedDate = modifyDate;
    }

    public void modify(String password, String nickname)
    {
        this.password = password;
        this.nickname = nickname;
    }

    public void setPost(Post post)
    {
        posts.add(post);
    }

    public void setComment(Comment comment)
    {
        comments.add(comment);
    }

    public void setLikedPostId(Long id)
    {
        likedPostIds.add(id);
    }

    public void ModifyPassword(String password)
    {
        this.password = password;
    }

    public void upExp()
    {
        exp++;

        if(exp >= level)
        {
            level++;
            exp = 0;
        }
    }

    public void downExp()
    {
        exp--;

        if(exp <= 0)
        {
            level--;
            exp = (level - 1);
        }
    }

    public void ban(boolean ban)
    {
        this.ban = ban;
    }

    public void setProfileImageBase64(String base64)
    {
        this.profileImageBase64 = base64;
    }

}
