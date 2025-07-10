package kr.co.myproject.dto.User;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UserProfileImageEdtiDto {
    private Long id;
    private String profileImageBase64;
}
