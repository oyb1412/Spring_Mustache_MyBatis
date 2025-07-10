package kr.co.myproject.dto.User;


import kr.co.myproject.controller.enums.Role;
import kr.co.myproject.entity.User;
import lombok.Getter;

@Getter
public class SessionUser {
    private Long id;
    private String nickname;
    private Role role;


    public SessionUser(User user)
    {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}
