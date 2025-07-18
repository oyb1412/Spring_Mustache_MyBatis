package kr.co.myproject.dto.User;

import kr.co.myproject.enums.FindPasswordQuestion;
import lombok.Getter;

@Getter
public class UserFindPasswordDto {
    private String username;
    private FindPasswordQuestion question;
    private String questionAnswer;
}
