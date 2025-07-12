package kr.co.myproject.dto.User;

import java.time.LocalDateTime;

import kr.co.myproject.entity.User;
import lombok.Getter;

@Getter
public class UserManagementDto {
    private Long id;
    private String nickname;
    private int level;
    private LocalDateTime createdDate;
    private boolean ban;

    public UserManagementDto(User user)
    {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.level = user.getLevel();
        this.createdDate = user.getCreatedDate();
        this.ban = user.isBan();
    }
}
