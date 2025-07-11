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

    private User user;

    private Board board;

    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Board board,  Long id, String title, String content,List<Comment> comments, User user, int likeCount, LocalDateTime createdDate, LocalDateTime modifiedDate)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.comments = comments;
        this.user = user;
        this.board = board;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.checkDate = LocalDateTime.now();
    }

    public void SetNotice()
    {
        this.isNotice = true;
    }

     public void SetComment(Comment comment)
    {
        comments.add(comment);
    }

       public void upLikeCount()
    {
        this.likeCount ++;
    }

    public void upViewCount()
    {
        this.viewCount++;
        this.recentViewCount++;
    }

    public void setRecentViewCount(int value)
    {
        this.recentViewCount = value;
    }

    public void setCheckDate(LocalDateTime time)
    {
        this.checkDate = time;
    }


    public void downLikeCount()
    {
        this.likeCount --;
    }

       public void modify(String title, String content, String filePath, String fileName)
    {
        this.title = title;
        this.content = content;
        this.filePath = filePath;
        this.fileName = fileName;
        this.modifiedDate = LocalDateTime.now();
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setFile(String filePath, String fileName)
    {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void setNew(boolean set)
    {
        this.isNew = set;
    }

    public void setHot(boolean set)
    {
        this.isHot = set;
    }
    
}
