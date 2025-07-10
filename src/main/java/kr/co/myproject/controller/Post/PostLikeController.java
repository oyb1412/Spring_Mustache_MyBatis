package kr.co.myproject.controller.Post;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.PostService;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class PostLikeController {
    private final PostService postService;
    private final UserService userService;

    @PatchMapping("/api/post/like")
    public Map<String, Object> postLike(@RequestParam Long id,
                                                      HttpSession session) {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");

        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }
        
        return postService.upLikeCount(id, sessionUser);
    }
    
     @PatchMapping("/api/post/dislike")
    public Map<String, Object> postDisLike(@RequestParam Long id,
                                                         HttpSession session) {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");

        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }

        return postService.downLikeCount(id, sessionUser);
    }
}
