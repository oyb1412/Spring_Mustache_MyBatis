package kr.co.myproject.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스를 상속받는 엔티티들은 이 클래스의 필드를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class) // JPA Auditing의 기능을 활성화시켜 생성일, 수정일을 자동으로 작동되게 함
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티가 생성될 때, 자동으로 현재 시간 저장
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate // 엔티티가 수정될 때, 자동으로 현재 시간으로 갱신
    protected LocalDateTime modifiedDate;
}