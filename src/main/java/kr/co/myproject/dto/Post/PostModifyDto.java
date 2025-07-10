package kr.co.myproject.dto.Post;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostModifyDto {

    private Long id;

    private String title;

    private String content;

    private String author;

    private MultipartFile file;

    public PostModifyDto(Long id, String title, String content, String author, MultipartFile file)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.file = file;
    }
}
