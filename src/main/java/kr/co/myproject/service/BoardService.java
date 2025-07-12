package kr.co.myproject.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.myproject.Mapper.BoardMapper;
import kr.co.myproject.Mapper.PostMapper;
import org.springframework.stereotype.Service;

import kr.co.myproject.dto.Board.BoardDto;
import kr.co.myproject.dto.Board.SidebarCategoryDto;
import kr.co.myproject.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;
    private final PostMapper postMapper;

    @Transactional
    public BoardDto findById(Long id)
    {
        Board board = boardMapper.findById(id);
        boardMapper.updateTodayPostCount(id, getTodayPostCount(id));
        return new BoardDto(board);
    }

    @Transactional
    public List<BoardDto> findAll()
    {
        List<Board> boards = boardMapper.findAll();
        for (Board board : boards) {
            boardMapper.updateTodayPostCount(board.getId(), getTodayPostCount(board.getId()));
        }
        return boards.stream()
                    .map(BoardDto::new)
                    .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardDto> findAll(int number)
    {
        List<Board> boards = boardMapper.findAll();
        for (Board board : boards) {
            boardMapper.updateTodayPostCount(board.getId(), getTodayPostCount(board.getId()));
        }
        List<BoardDto> boardDtos = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            boardDtos.add(new BoardDto(boards.get(i)));
        }

        return boardDtos;
    }

    @Transactional
    public List<BoardDto> findTop3()
    {
        List<Board> boards = boardMapper.findTop3ByOrderByTodayPostCountDesc();
        for (Board board : boards) {
            boardMapper.updateTodayPostCount(board.getId(), getTodayPostCount(board.getId()));
        }
        return boards.stream()
                     .map(BoardDto::new)
                     .collect(Collectors.toList());
    }

    @Transactional
    public int getTodayPostCount(Long boardId) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay(); // 00:00:00
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay(); // 내일 00:00:00

        return postMapper.countTodayPosts(boardId, todayStart, tomorrowStart);
    }

    @Transactional
    public List<SidebarCategoryDto> findSidebarCategoryAll()
    {
        List<Board> boards = boardMapper.findAll();
        List<SidebarCategoryDto> dtos = new ArrayList<>();
        for (Board board : boards) {
            dtos.add(new SidebarCategoryDto(board.getId(), board.getName(), board.getTotalPostCount()));
        }
        return dtos;
    }
}
