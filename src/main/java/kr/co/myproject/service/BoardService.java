package kr.co.myproject.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.co.myproject.dto.Board.BoardDto;
import kr.co.myproject.dto.Board.SidebarCategoryDto;
import kr.co.myproject.entity.Board;
import kr.co.myproject.repisitory.BoardRepository;
import kr.co.myproject.repisitory.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    public BoardDto findById(Long id)
    {
        Board board =  boardRepository.findById(id).orElseThrow();
        board.setTodayPostCount(getTodayPostCount(id));
        return new BoardDto(board);
    }

    public List<BoardDto> findAll()
    {
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            board.setTodayPostCount(getTodayPostCount(board.getId()));
        }
        return boards.stream()
                    .map(BoardDto::new)
                    .collect(Collectors.toList());
    }

    public List<BoardDto> findAll(int number)
    {
        List<Board> boards = boardRepository.findAll();
        for (Board board : boards) {
            board.setTodayPostCount(getTodayPostCount(board.getId()));
        }
        List<BoardDto> boardDtos = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            boardDtos.add(new BoardDto(boards.get(i)));
        }

        return boardDtos;
    }

    public List<BoardDto> findTop3()
    {
        List<Board> boards = boardRepository.findTop3ByOrderByTodayPostCountDesc();
        for (Board board : boards) {
            board.setTodayPostCount(getTodayPostCount(board.getId()));
        }
        return boards.stream()
                     .map(BoardDto::new)
                     .collect(Collectors.toList());
    }

    public int getTodayPostCount(Long boardId) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay(); // 00:00:00
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay(); // 내일 00:00:00

        return postRepository.countTodayPosts(boardId, todayStart, tomorrowStart);
    }

    public List<SidebarCategoryDto> findSidebarCategoryAll()
    {
        List<Board> boards = boardRepository.findAll();
        List<SidebarCategoryDto> dtos = new ArrayList<>();
        for (Board board : boards) {
            dtos.add(new SidebarCategoryDto(board.getId(), board.getName(), board.getTotalPostCount()));
        }

        return dtos;
    }
}
