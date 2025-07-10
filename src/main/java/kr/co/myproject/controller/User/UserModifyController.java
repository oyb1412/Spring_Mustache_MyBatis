package kr.co.myproject.controller.User;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.dto.User.UserModifyDto;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class UserModifyController {
    private final UserService userService;
    
    @PutMapping("/api/user/modify")
    public Map<String, Object> postMethodName(@RequestBody UserModifyDto dto,
                                               HttpSession session)
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");

        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }

        return userService.modify(dto, sessionUser);
    }
    
}
