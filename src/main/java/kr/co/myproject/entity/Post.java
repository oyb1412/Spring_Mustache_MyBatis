package kr.co.myproject.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity{
    private Long id;

    private Long userId;

    private Long boardId;

    private String title;

    private String content;

    private int likeCount;

    private int viewCount;

    private boolean isNotice;

    private boolean isHot = false;

    private boolean isNew = true;

    private String filePath;

    private String fileName;

    private LocalDateTime checkDate = LocalDateTime.now();

    private int recentViewCount = 0;


    @Builder
    public Post(Long id, Long userId, Long boardId, String title, String content,  int likeCount, LocalDateTime createdDate, LocalDateTime modifiedDate)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.boardId = boardId;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.checkDate = LocalDateTime.now();
    }
}
