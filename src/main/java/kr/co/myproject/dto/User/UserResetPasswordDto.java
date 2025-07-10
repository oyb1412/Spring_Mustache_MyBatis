package kr.co.myproject.dto.User;

import lombok.Getter;

@Getter
public class UserResetPasswordDto {
    private String newPassword;
    private String confirmPassword;
}
