package kr.co.myproject.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private boolean isNotice;

    @Column(nullable = false)
    private boolean isHot = false;

    @Column(nullable = false)
    private boolean isNew = true;

    @Column(nullable = true)
    private String filePath;

    @Column(nullable = true)
    private String fileName;

    @Column(nullable = true)
    private LocalDateTime checkDate = LocalDateTime.now();

    @Column(nullable = false)
    private int recentViewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
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
