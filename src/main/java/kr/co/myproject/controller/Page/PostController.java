package kr.co.myproject.controller.Page;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.Util.ModelUtil;
import kr.co.myproject.dto.Board.BoardDto;
import kr.co.myproject.dto.Post.PostDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.dto.User.UserDto;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.PostService;
import kr.co.myproject.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {
    private final ModelUtil modelUtil;
    private final PostService postService;
    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/postWrite-page/{boardId}")
    public String postWritePage(@PathVariable Long boardId,
                                  HttpSession session,
                                  Model model) 
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        BoardDto board = boardService.findById(boardId);
        UserDto user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);
        model.addAttribute("board", board);
        return "post/postWritePage"; 
    }
    
    @GetMapping("/postEdit-page/{id}")
    public String getMethodName(@PathVariable Long id,
                                 Model model) {
        PostDto post = postService.findById(id);
        BoardDto board = boardService.findById(post.getBoard().getId());

        model.addAttribute("board", board);
        model.addAttribute("post", post);
        return "post/postEditPage";
    }

    @GetMapping("/post-page/{id}")
    public String postPage(@PathVariable Long id,
                                 Model model,
                                 HttpSession session)
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");

        if(sessionUser == null)
        {
            modelUtil.SetError(model, "유저 정보가 올바르지 않습니다");
            return "other/index";
        }


        PostDto post = postService.findById(id);

        if(post == null)
        {
            modelUtil.SetError(model, "글 정보가 올바르지 않습니다");
            return "other/index";
        }

        if(sessionUser.getNickname().equals(post.getAuthor()))
        {
            model.addAttribute("isAuthor", true);
        }

        model.addAttribute("post", post);
        model.addAttribute("board", post.getBoard());
        
        postService.upViewCount(id);

        return "post/post";
    }

    @GetMapping("/postSearch-page")
    public String searchPage(@RequestParam String keyword,
                              Model model) {
        List<PostDto> posts = postService.search(keyword);
        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        return "post/searchPage";
    }
}
