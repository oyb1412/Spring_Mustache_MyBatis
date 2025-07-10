package kr.co.myproject.Util;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.controller.enums.Role;
import kr.co.myproject.dto.Board.SidebarCategoryDto;
import kr.co.myproject.dto.Post.PopularPostListDto;
import kr.co.myproject.dto.Post.SidebarNoticeDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.dto.User.UserDto;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.PostService;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {
    private final BoardService boardService;
    private final PostService postService;
    private final UserService userService;

    @ModelAttribute
    public void addUserToModel(Model model, HttpSession session) {
        SessionUser sessionUser = (SessionUser) session.getAttribute("user");

        if (sessionUser != null) {
            UserDto user = userService.findByNickname(sessionUser.getNickname());

            if (user != null) {
                model.addAttribute("user", user);

                if(user.getRole() == Role.ADMIN)
                {
                    model.addAttribute("isAdmin", true);
                }
            }
        }
    }


    @ModelAttribute("popularTop5Post")
    public List<PopularPostListDto> popularTop5Post() {
        List<PopularPostListDto> dto = postService.findPopularTop5Post();

        return dto;
    }

    @ModelAttribute("sidebarCategoryList")
    public List<SidebarCategoryDto> sidebarCategoryList() {
        List<SidebarCategoryDto> dtos = boardService.findSidebarCategoryAll();

       return dtos;
    }

    @ModelAttribute("sidebarNoticeList")
    public List<SidebarNoticeDto> sidebarNoticeList() {
    List<SidebarNoticeDto> dto = postService.findSidebarNoticeList();

    return dto;
}
}
