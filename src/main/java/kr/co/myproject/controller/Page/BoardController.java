package kr.co.myproject.controller.Page;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
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
public class BoardController {
    private final PostService postService;
    private final UserService userService;
    private final BoardService boardService;

    
    @GetMapping("/boards-page")
    public String boardsPage(Model model,
                             HttpSession session) 
    {
        SessionUser sessionUser = (SessionUser)session.getAttribute("user");
        List<BoardDto> boardList = boardService.findAll();
        boardList.removeIf(board -> board.getId().equals(9L));
        UserDto user = userService.findById(sessionUser.getId());

        model.addAttribute("user", user);
        model.addAttribute("boardList", boardList);
        return "board/boards";
    }

    @GetMapping("/board-page/{id}")
    public String boardPage(@PathVariable Long id,
                             Model model) 
    {
        List<PostDto> posts;
        if(id.equals(9L))
        {
            posts = postService.findPopularPosts();
        }
        else
        {
            posts = postService.findAllByBoardId(id); 
        }

         //모델에 board 정보 넘기기
        BoardDto dto = boardService.findById(id);

        model.addAttribute("board", dto);
        model.addAttribute("posts", posts);
        return "board/board";
    }
}
