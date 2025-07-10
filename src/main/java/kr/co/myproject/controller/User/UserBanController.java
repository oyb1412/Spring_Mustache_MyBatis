package kr.co.myproject.controller.User;

import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserBanController {
    private final UserService userService;
    
    @PatchMapping("/api/user/ban")
    public Map<String, Object> userBan(@RequestParam Long id, 
                                        HttpSession session)
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        return userService.ban(id, sessionUser);
    }

    @PatchMapping("/api/user/unban")
    public Map<String, Object> userUnban(@RequestParam Long id,
                                          HttpSession session)
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        return userService.unban(id, sessionUser);
    }
}
