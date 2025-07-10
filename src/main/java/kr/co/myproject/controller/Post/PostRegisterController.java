package kr.co.myproject.controller.Post;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.myproject.dto.Post.PostRegisterDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
public class PostRegisterController {
    private final PostService postService;

    @PostMapping("/api/post/register")
    public Map<String, Object> postRegister(@RequestParam("title") String title,
                                            @RequestParam("content") String content,
                                            @RequestParam("boardId") Long boardId,
                                            @RequestParam(value = "file", required = false) MultipartFile file,
                                            HttpSession session) 
    {
        //현재 유저정보 
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        
        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }

        PostRegisterDto dto = new PostRegisterDto(boardId, title, content, file);

        //포스트 등록(내부에서 author등록)
        return postService.register(dto, sessionUser);
    }
}
