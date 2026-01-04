package com.planner.my.dto;

import com.planner.my.entity.Priority;
import com.planner.my.entity.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "월간 계획 생성/수정 요청")
public class MonthlyPlanRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "계획 제목", example = "영어 공부 마스터", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "계획 상세 설명", example = "매일 1시간씩 영어 회화 학습")
    private String description;

    @NotNull(message = "Year is required")
    @Schema(description = "연도", example = "2025", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer year;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    @Schema(description = "월 (1-12)", example = "12", minimum = "1", maximum = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer month;

    @NotNull(message = "Priority is required")
    @Schema(description = "우선순위 (HIGH, MEDIUM, LOW)", example = "MEDIUM", requiredMode = Schema.RequiredMode.REQUIRED)
    private Priority priority;

    @Schema(description = "계획 상태 (NOT_STARTED, IN_PROGRESS, COMPLETED, FAILED)", example = "NOT_STARTED")
    private PlanStatus status;
}