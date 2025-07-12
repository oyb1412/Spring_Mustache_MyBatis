package kr.co.myproject.dto.Post;

import org.springframework.web.multipart.MultipartFile;

import kr.co.myproject.entity.Board;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRegisterDto {
    private Long boardId;
    
    private String title;

    private String content;

    private MultipartFile file;

    public PostRegisterDto(Long boardId, String title, String content, MultipartFile file)
    {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.file = file;
    }

    public Post buildPostEntity(User user, Board board)
    {
        return Post.builder()
                   .title(title)
                   .content(content)
                   .boardId(board.getId())
                   .userId(user.getId())
                   .build();
    }
}
