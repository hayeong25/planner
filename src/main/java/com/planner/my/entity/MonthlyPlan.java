package com.planner.my.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 월간 계획을 나타내는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "monthly_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MonthlyPlan extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "plan_year", nullable = false)
    private Integer year;

    @Column(name = "plan_month", nullable = false)
    private Integer month;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
}