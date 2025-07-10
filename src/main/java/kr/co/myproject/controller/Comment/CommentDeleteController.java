package kr.co.myproject.controller.Comment;

import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.dto.Comment.CommentDeleteDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.CommentService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommentDeleteController {
    
    private final CommentService commentService;
    
    @DeleteMapping("/api/comment/delete")
    public Map<String, Object> commentDelete(@RequestBody CommentDeleteDto dto,
                                              HttpSession session,
                                              Model model)
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");

        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }

        return commentService.delete(dto, sessionUser);
    }
}
