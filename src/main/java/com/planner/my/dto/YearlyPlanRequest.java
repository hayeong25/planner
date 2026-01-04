package com.planner.my.dto;

import com.planner.my.entity.Priority;
import com.planner.my.entity.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "연간 계획 생성/수정 요청")
public class YearlyPlanRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "계획 제목", example = "건강한 생활습관 만들기", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "계획 상세 설명", example = "규칙적인 운동과 식단 관리로 건강 유지")
    private String description;

    @NotNull(message = "Year is required")
    @Schema(description = "연도", example = "2025", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer year;

    @NotNull(message = "Priority is required")
    @Schema(description = "우선순위 (HIGH, MEDIUM, LOW)", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    private Priority priority;

    @Schema(description = "계획 상태 (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)", example = "NOT_STARTED")
    private PlanStatus status;
}