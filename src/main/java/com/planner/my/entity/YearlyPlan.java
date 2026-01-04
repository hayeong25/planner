package com.planner.my.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 연간 계획을 나타내는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "yearly_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class YearlyPlan extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "plan_year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
}