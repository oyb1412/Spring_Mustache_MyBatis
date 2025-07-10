package kr.co.myproject.controller.User;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.myproject.dto.User.UserRegisterDto;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserRegisterController {
    private final UserService userService;

    @PostMapping("/api/user/register")
    public Map<String, Object> userRegister(@Valid @RequestBody UserRegisterDto dto,
                                                                BindingResult result) {
        //기본 유효성 검사
        if(result.hasErrors())
        {
            return Map.of("success", false, "message", result.getFieldError().getDefaultMessage());
        }

        //유저 등록
        return userService.register(dto);
    }
}
