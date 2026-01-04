package com.planner.my.dto;

import com.planner.my.entity.YearlyPlan;
import com.planner.my.entity.Priority;
import com.planner.my.entity.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "연간 계획 응답")
public class YearlyPlanResponse {

    @Schema(description = "계획 ID", example = "1")
    private Long id;

    @Schema(description = "계획 제목", example = "건강한 생활습관 만들기")
    private String title;

    @Schema(description = "계획 상세 설명", example = "규칙적인 운동과 식단 관리로 건강 유지")
    private String description;

    @Schema(description = "연도", example = "2025")
    private Integer year;

    @Schema(description = "우선순위", example = "HIGH")
    private Priority priority;

    @Schema(description = "계획 상태", example = "NOT_STARTED")
    private PlanStatus status;

    @Schema(description = "생성 일시", example = "2025-12-21T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-12-21T10:30:00")
    private LocalDateTime updatedAt;

    public static YearlyPlanResponse from(YearlyPlan entity) {
        return YearlyPlanResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .year(entity.getYear())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}