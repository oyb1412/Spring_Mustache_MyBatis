package kr.co.myproject.controller.Page;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.dto.User.UserDto;
import kr.co.myproject.entity.User;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin-page")
    public String adminPage(HttpSession session,
                            Model model) 
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        UserDto user = userService.findById(sessionUser.getId());
        List<UserDto> users = userService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "user/adminPage";
    }
    
    
}
