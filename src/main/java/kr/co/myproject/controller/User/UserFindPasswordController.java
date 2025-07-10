package kr.co.myproject.controller.User;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.User.UserFindPasswordDto;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class UserFindPasswordController {
    private final UserService userService;
    
    @GetMapping("/api/user/findPassword")
    public Map<String, Object> findPassword(@RequestBody UserFindPasswordDto dto,
                                             HttpSession session)  
    {
        return userService.findPassword(dto, session);
    }
}
