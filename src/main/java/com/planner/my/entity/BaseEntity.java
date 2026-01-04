package com.planner.my.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 모든 계획 엔티티의 공통 필드와 생명주기 콜백을 정의하는 추상 클래스입니다.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanStatus status;

    @Builder.Default
    @Column(nullable = false)
    private Integer displayOrder = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 엔티티 생성 시 호출되는 콜백 메서드입니다.
     * 생성 시간을 설정하고, 상태와 표시 순서의 기본값을 지정합니다.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = PlanStatus.NOT_STARTED;
        }
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }

    /**
     * 엔티티 수정 시 호출되는 콜백 메서드입니다.
     * 수정 시간을 갱신합니다.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
