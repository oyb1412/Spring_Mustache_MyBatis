package kr.co.myproject.dto.Board;

import lombok.Getter;

@Getter
public class SidebarCategoryDto {
    private Long id;
    private String name;
    private int totalPostCount;

    public SidebarCategoryDto(Long id,String name, int totalPostCount) {
        this.id = id;
        this.name = name;
        this.totalPostCount = totalPostCount;
    }

}
