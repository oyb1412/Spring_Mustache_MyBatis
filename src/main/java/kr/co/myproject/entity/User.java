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

    private int level;

    private int exp;

    private boolean ban ;

    private String provider;

    private String providerId;

    private String profileImageBase64;

    private FindPasswordQuestion findPasswordQuestion;

    private String findPasswordQuestionAnswer;

    private Role role;

    @Builder
    public User(Long id, String username, String password, String nickname,
    String provider, String providerId, Role role,
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
        this.findPasswordQuestion = findPasswordQuestion;
        this.findPasswordQuestionAnswer = findPasswordQuestionAnswer;
        this.createdDate = createDate;
        this.modifiedDate = modifyDate;
    }

}
