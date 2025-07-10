package kr.co.myproject.controller.User;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserWithdrawalController {
    private final UserService userService;

    @DeleteMapping("/api/user/withdrawal")
    public Map<String, Object> userWithdrawal(@RequestParam Long id,
                                               HttpSession session)
    {
        return userService.withdrawal(id, session);
    }
}
