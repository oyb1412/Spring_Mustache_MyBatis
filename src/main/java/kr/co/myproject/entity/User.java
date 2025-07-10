package kr.co.myproject.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kr.co.myproject.controller.enums.FindPasswordQuestion;
import kr.co.myproject.controller.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 파라미터 없는 기본 생성자 자동 생성(JPA 필수)
@Entity // JPA가 이 클래스를 테이블과 매핑되는 객체로 인식
public class User extends BaseTimeEntity {
    
    @Id // 이 필드가 테이블의 기본 키(PK)임을 나타냄
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENT설정
    private Long id;

    @Column(length = 12, nullable = false)
    private String username;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 12, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private int exp = 0;

    @Column(nullable = false)
    private boolean ban = false;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    @Column(columnDefinition = "LONGTEXT" , nullable = true)
    private String profileImageBase64;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FindPasswordQuestion findPasswordQuestion;

    @Column(nullable = false)
    private String findPasswordQuestionAnswer;

    @Enumerated(EnumType.STRING) //꼭 STRING ! ORDINAL 쓰면 순서 바뀌면 다 꼬임
    @Column(nullable = false)
    private Role role = Role.MEMBER; // DEFAULT값 지정

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

     @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection
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
