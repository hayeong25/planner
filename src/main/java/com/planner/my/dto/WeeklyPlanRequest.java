package com.planner.my.dto;

import com.planner.my.entity.Priority;
import com.planner.my.entity.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "주간 계획 생성/수정 요청")
public class WeeklyPlanRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "계획 제목", example = "프로젝트 마일스톤 완료", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "계획 상세 설명", example = "API 개발 및 테스트 완료하기")
    private String description;

    @NotNull(message = "Week start date is required")
    @Schema(description = "주 시작 날짜 (월요일)", example = "2025-12-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate weekStartDate;

    @NotNull(message = "Week end date is required")
    @Schema(description = "주 종료 날짜 (일요일)", example = "2025-12-21", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate weekEndDate;

    @NotNull(message = "Priority is required")
    @Schema(description = "우선순위 (HIGH, MEDIUM, LOW)", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    private Priority priority;

    @Schema(description = "계획 상태 (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)", example = "NOT_STARTED")
    private PlanStatus status;
}