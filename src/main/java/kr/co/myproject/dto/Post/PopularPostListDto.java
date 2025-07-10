package kr.co.myproject.dto.Post;

import lombok.Getter;

@Getter
public class PopularPostListDto {
    private Long id;
    private String title;
    private String category;
    private int viewCount;

    public PopularPostListDto(Long id,String title, String category, int viewCount)
    {
        this.id = id;
        this.title = title;
        this.category = category;
        this.viewCount = viewCount;
    }
}
