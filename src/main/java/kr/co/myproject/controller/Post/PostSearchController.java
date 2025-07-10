package kr.co.myproject.controller.Post;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.Post.PostDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class PostSearchController {
    private final PostService postService;

    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String keyword,
                                       HttpSession session) 
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");

        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "로그인이 필요한 서비스입니다");
        }

        List<PostDto> posts = postService.search(keyword);
        if(posts.size() > 0)
        {
            return Map.of("success", true, "message", posts.size() + "건의 검색 결과가 있습니다",
            "keyword", keyword);
        }
        else
        {
            return Map.of("success", false, "message", "검색 결과가 없습니다");
        }
    }
    
}
