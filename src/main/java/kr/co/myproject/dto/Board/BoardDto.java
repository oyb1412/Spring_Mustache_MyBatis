package kr.co.myproject.dto.Board;

import kr.co.myproject.entity.Board;
import lombok.Getter;

@Getter
public class BoardDto {
    private Long id;
    private String name;
    private String description;
    private int todayPostCount;
    private int totalPostCount;


    public BoardDto(Board board)
    {
        this.id = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
        this.todayPostCount = board.getTodayPostCount();
        this.totalPostCount = board.getTotalPostCount();
    }

}
