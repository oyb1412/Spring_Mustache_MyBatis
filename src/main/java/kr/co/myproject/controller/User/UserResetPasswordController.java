package kr.co.myproject.controller.User;

import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.User.UserResetPasswordDto;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserResetPasswordController {
    private final UserService userService;

    @PatchMapping("/api/user/resetPassword")
    public Map<String, Object> resetPassword(@RequestBody UserResetPasswordDto dto,
                                              HttpSession session)
    {
        return userService.resetPassword(dto, session);
    }
}
