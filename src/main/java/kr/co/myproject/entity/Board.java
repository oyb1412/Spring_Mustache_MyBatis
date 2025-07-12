package kr.co.myproject.entity;

import java.util.ArrayList;
import java.util.List;

import kr.co.myproject.enums.BoardCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Board {
    private Long id;

    private String name;

    private String code;

    private String description;

    private int todayPostCount;

    private int totalPostCount;

    private BoardCategory category;
}
