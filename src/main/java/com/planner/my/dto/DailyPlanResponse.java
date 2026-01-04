package com.planner.my.dto;

import com.planner.my.entity.DailyPlan;
import com.planner.my.entity.Priority;
import com.planner.my.entity.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "일간 계획 응답")
public class DailyPlanResponse {

    @Schema(description = "계획 ID", example = "1")
    private Long id;

    @Schema(description = "계획 제목", example = "운동하기")
    private String title;

    @Schema(description = "계획 상세 설명", example = "30분 조깅 후 스트레칭")
    private String description;

    @Schema(description = "계획 날짜", example = "2025-12-21")
    private LocalDate planDate;

    @Schema(description = "우선순위", example = "HIGH")
    private Priority priority;

    @Schema(description = "계획 상태", example = "NOT_STARTED")
    private PlanStatus status;

    @Schema(description = "생성 일시", example = "2025-12-21T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-12-21T10:30:00")
    private LocalDateTime updatedAt;

    public static DailyPlanResponse from(DailyPlan entity) {
        return DailyPlanResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .planDate(entity.getPlanDate())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}