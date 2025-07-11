package kr.co.myproject.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.co.myproject.enums.FindPasswordQuestion;
import kr.co.myproject.enums.Role;
import kr.co.myproject.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterDto {

    @NotBlank(message= "아이디는 필수입니다")
    @Size(min = 5, max = 12, message= "아이디는 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문/숫자만 입력 가능합니다")
    private String username;

    @NotBlank(message= "비밀번호는 필수입니다")
    @Size(min = 5, max = 12, message= "비밀번호는 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "비밀번호는 영문/숫자만 입력 가능합니다")
    private String password;

    @NotBlank(message= "확인용 비밀번호는 필수입니다")
    @Size(min = 5, max = 12, message= "비밀번호는 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "비밀번호는 영문/숫자만 입력 가능합니다")
    private String checkPassword;

    @NotBlank(message= "닉네임은 필수입니다")
    @Size(min = 5, max = 12, message= "닉네임은 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "닉네임은 영문/숫자만 입력 가능합니다")
    private String nickname;

    @NotNull(message= "비밀번호 힌트를 골라주세요")
    private FindPasswordQuestion question;

    @NotBlank(message= "비밀번호 힌트 정답은 필수입니다")
    @Size(min = 1, max = 12, message= "비밀번호 힌트 정답은 12자 이하로 지어주세요")
    private String questionAnswer;


    public User buildUserEntity(String setPassword)
    {
        return User.builder()
                   .username(username)
                   .password(setPassword)
                   .nickname(nickname)
                   .role(Role.MEMBER)
                   .findPasswordQuestion(question)
                   .findPasswordQuestionAnswer(questionAnswer)
                   .build();
    }
}
