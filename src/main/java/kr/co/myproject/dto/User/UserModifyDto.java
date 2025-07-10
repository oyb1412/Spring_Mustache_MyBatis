package kr.co.myproject.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserModifyDto {
    @NotBlank(message= "새 비밀번호는 필수입니다")
    @Size(min = 5, max = 12, message= "새 비밀번호는 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "새 비밀번호는 영문/숫자만 입력 가능합니다")
    private String newPassword;

    @NotBlank(message= "새 비밀번호는 필수입니다")
    @Size(min = 5, max = 12, message= "새 비밀번호는 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "새 비밀번호는 영문/숫자만 입력 가능합니다")
    private String confirmPassword;

    @NotBlank(message= "닉네임은 필수입니다")
    @Size(min = 5, max = 12, message= "닉네임은 5자 이상 12자 이하로 지어주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "닉네임은 영문/숫자만 입력 가능합니다")
    private String newNickname;
}
