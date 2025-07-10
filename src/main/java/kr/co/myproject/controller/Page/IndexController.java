package kr.co.myproject.controller.Page;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.myproject.dto.Board.BoardDto;
import kr.co.myproject.dto.Post.PostDto;
import kr.co.myproject.service.BoardService;
import kr.co.myproject.service.PostService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostService postService;
    private final BoardService boardService;
    
    @GetMapping("/")
    public String index(Model model) 
    {
        List<BoardDto> boardList = boardService.findAll(6);
        model.addAttribute("boardList", boardList);

        List<BoardDto> popularBoardList = boardService.findTop3();
        model.addAttribute("popularBoardList", popularBoardList);

        List<PostDto> popularPosts = postService.findPopularPosts();
        model.addAttribute("popularPosts", popularPosts);

        postService.updatePostState();
        return "other/index";
    }
}
