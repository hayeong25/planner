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
@Schema(description = "일간 계획 생성/수정 요청")
public class DailyPlanRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "계획 제목", example = "운동하기", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "계획 상세 설명", example = "30분 조깅 후 스트레칭")
    private String description;

    @NotNull(message = "Plan date is required")
    @Schema(description = "계획 날짜", example = "2025-12-21", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate planDate;

    @NotNull(message = "Priority is required")
    @Schema(description = "우선순위 (HIGH, MEDIUM, LOW)", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    private Priority priority;

    @Schema(description = "계획 상태 (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)", example = "NOT_STARTED")
    private PlanStatus status;
}