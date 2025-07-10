package kr.co.myproject.controller.Post;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import kr.co.myproject.dto.Post.PostModifyDto;
import kr.co.myproject.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
public class PostModifyController {
    private final PostService postService;
    
    @PutMapping("/api/post/modify")
    public Map<String, Object> postModify(@RequestParam("id") Long id,
                                          @RequestParam("title") String title,
                                          @RequestParam("content") String content,
                                          @RequestParam("author") String author,
                                          @RequestParam(value="file",  required = false) MultipartFile file)
    {
        PostModifyDto dto = new PostModifyDto(id, title, content, author, file);
        return postService.modify(dto);
    }
}
