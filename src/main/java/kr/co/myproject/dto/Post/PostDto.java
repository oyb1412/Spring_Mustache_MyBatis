package kr.co.myproject.dto.Post;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kr.co.myproject.dto.Board.BoardDto;
import kr.co.myproject.dto.Comment.CommentDto;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private int userLevel;

    private String author;
    private boolean isHot;
    private boolean isNew;
    private boolean isNotice;
    private int commentCount;
    private String authorProfileImage;
    private String filePath;
    private String fileName;

    private int viewCount;
    private String createdDate;
    private int likeCount;
    private BoardDto board;

    private List<CommentDto> commentsDto = new ArrayList<>();

    public PostDto(Post post)
    {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.userLevel = post.getUser().getLevel();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.author = post.getUser().getNickname();
        this.filePath = post.getFilePath();
        this.fileName = post.getFileName();
        this.isHot = post.isHot();
        this.isNew = post.isNew();
        this.board = new BoardDto(post.getBoard());
        this.isNotice = post.isNotice();
        this.authorProfileImage = post.getUser().getProfileImageBase64();
        this.likeCount = post.getLikeCount();
        for (Comment child : post.getComments()) {
            this.commentsDto.add(new CommentDto(child, true));
        }

        this.commentCount = commentsDto.size();
    }


}
