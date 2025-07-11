package kr.co.myproject.entity;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public abstract class BaseTimeEntity {
    protected LocalDateTime createdDate;
    protected LocalDateTime modifiedDate;
}