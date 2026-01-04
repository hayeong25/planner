package com.planner.my.dto;

import com.planner.my.entity.PlanStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "계획 상태 변경 요청")
public class StatusUpdateRequest {

    @NotNull(message = "Status is required")
    @Schema(description = "변경할 상태 (NOT_STARTED: 시작 전, IN_PROGRESS: 진행 중, COMPLETED: 완료, FAILED: 실패)",
            example = "COMPLETED", requiredMode = Schema.RequiredMode.REQUIRED)
    private PlanStatus status;
}