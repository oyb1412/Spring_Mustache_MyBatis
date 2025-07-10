package kr.co.myproject.controller.Comment;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.co.myproject.dto.Comment.CommentRegisterDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.CommentService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
public class CommentRegisterController {

    private final CommentService commentService;

    @PostMapping("/api/comment/register")
    public Map<String, Object> postMethodName(@Valid @RequestBody CommentRegisterDto dto,
                                                                  BindingResult result,
                                                                  HttpSession session,
                                                                  Model model) 
    {
        //기본 유효성 검사
        if(result.hasErrors())
        {
            return Map.of("success", false, "message", result.getFieldError().getDefaultMessage());
        }

        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        
        if(sessionUser == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }
        
        return commentService.register(dto, sessionUser);
    }
}
