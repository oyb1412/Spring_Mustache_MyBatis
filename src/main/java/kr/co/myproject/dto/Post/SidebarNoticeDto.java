package kr.co.myproject.dto.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class SidebarNoticeDto {
    private Long id;
    private String title;
    private String date;

    public SidebarNoticeDto(Long id,String title, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));;
    }

}
