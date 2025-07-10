package kr.co.myproject.controller.Page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.dto.User.UserDto;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;




@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/userRegister-page")
    public String userRegisterPage() {
        return "user/userRegister";
    }

    @GetMapping("/userLogin-page")
    public String userLoginPage() {
        return "user/userLogin";
    }

    @GetMapping("/findPassword-page")
    public String findPasswordPage() {
        return "user/findPassword";
    }

    @GetMapping("/resetPassword-page")
    public String resetPasswordPage() {
        return "user/resetPassword";
    }

    @GetMapping("/my-page")
    public String myPage(HttpSession session,
                         Model model) 
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        UserDto user = userService.findById(sessionUser.getId());

        model.addAttribute("user", user);
        return "user/myPage";
    }

    @GetMapping("/userEdit-page")
    public String userEdit(HttpSession session,
                           Model model) {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        UserDto user = userService.findById(sessionUser.getId());

        model.addAttribute("user", user);
        return "user/userEdit";
    }
    
    @GetMapping("/userDetail-page/{id}")
    public String userDetail(@PathVariable Long id,
                              Model model) 
    {
        UserDto user = userService.findById(id);
        model.addAttribute("user", user);

        return "user/userDetail";
    }

    @GetMapping("/edit-image-page")
    public String editImagePage(@RequestParam Long id,
                                 Model model) 
    {
        UserDto user = userService.findById(id);
        model.addAttribute("user", user);                                
        return "user/editImage";
    }
    
    
    
}
