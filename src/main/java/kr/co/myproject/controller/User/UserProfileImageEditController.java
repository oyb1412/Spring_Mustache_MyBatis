package kr.co.myproject.controller.User;

import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.myproject.dto.User.UserProfileImageEdtiDto;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserProfileImageEditController {
    private final UserService userService;

    @PatchMapping("/api/imageUpload")
    public Map<String, Object> imageUpload(@RequestBody UserProfileImageEdtiDto dto)
    {
        return userService.imageUpload(dto);
    }
}
