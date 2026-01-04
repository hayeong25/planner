package com.planner.my.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "순서 변경 요청")
public class ReorderRequest {

    @NotNull
    @Schema(description = "새로운 순서대로 정렬된 계획 ID 목록", example = "[3, 1, 2]")
    private List<Long> orderedIds;
}
