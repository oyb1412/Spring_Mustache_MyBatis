package kr.co.myproject.controller.Post;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.myproject.service.PostService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PostDeleteController {
    private final PostService postService;
    
    @DeleteMapping("/api/post/delete")
    public Map<String, Object> postDelete(@RequestParam Long id)
    {
        return postService.delete(id);
    }
}
