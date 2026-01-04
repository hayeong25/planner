package com.planner.my.controller;

import com.planner.my.dto.ReorderRequest;
import com.planner.my.dto.YearlyPlanRequest;
import com.planner.my.dto.YearlyPlanResponse;
import com.planner.my.dto.StatusUpdateRequest;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.service.YearlyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yearly")
@RequiredArgsConstructor
@Tag(name = "Yearly Plan", description = "연간 계획 관리 API")
public class YearlyController {

    private final YearlyService yearlyService;

    @PostMapping
    @Operation(summary = "연간 계획 생성", description = "새로운 연간 계획을 생성합니다.")
    public ResponseEntity<YearlyPlanResponse> create(@Valid @RequestBody YearlyPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(yearlyService.create(request));
    }

    @GetMapping
    @Operation(summary = "전체 연간 계획 조회", description = "모든 연간 계획을 조회합니다.")
    public ResponseEntity<List<YearlyPlanResponse>> findAll() {
        return ResponseEntity.ok(yearlyService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "연간 계획 단건 조회", description = "ID로 특정 연간 계획을 조회합니다.")
    public ResponseEntity<YearlyPlanResponse> findById(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(yearlyService.findById(id));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "연도별 계획 조회", description = "특정 연도의 모든 연간 계획을 조회합니다.")
    public ResponseEntity<List<YearlyPlanResponse>> findByYear(
            @Parameter(description = "연도", example = "2025") @PathVariable Integer year) {
        return ResponseEntity.ok(yearlyService.findByYear(year));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 연간 계획 조회", description = "특정 상태의 모든 연간 계획을 조회합니다.")
    public ResponseEntity<List<YearlyPlanResponse>> findByStatus(
            @Parameter(description = "계획 상태", example = "NOT_STARTED") @PathVariable PlanStatus status) {
        return ResponseEntity.ok(yearlyService.findByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "우선순위별 연간 계획 조회", description = "특정 우선순위의 모든 연간 계획을 조회합니다.")
    public ResponseEntity<List<YearlyPlanResponse>> findByPriority(
            @Parameter(description = "우선순위", example = "HIGH") @PathVariable Priority priority) {
        return ResponseEntity.ok(yearlyService.findByPriority(priority));
    }

    @PutMapping("/{id}")
    @Operation(summary = "연간 계획 수정", description = "기존 연간 계획을 수정합니다.")
    public ResponseEntity<YearlyPlanResponse> update(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody YearlyPlanRequest request) {
        return ResponseEntity.ok(yearlyService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "연간 계획 상태 변경", description = "계획의 달성 상태만 변경합니다.")
    public ResponseEntity<YearlyPlanResponse> updateStatus(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(yearlyService.updateStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "연간 계획 삭제", description = "연간 계획을 삭제합니다.")
    public ResponseEntity<Void> delete(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        yearlyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 연간 계획의 표시 순서를 재정렬합니다.
     *
     * @param request 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 연간 계획 목록
     */
    @PutMapping("/reorder")
    @Operation(summary = "연간 계획 순서 변경", description = "드래그 앤 드롭으로 계획 순서를 변경합니다.")
    public ResponseEntity<List<YearlyPlanResponse>> reorder(@Valid @RequestBody ReorderRequest request) {
        return ResponseEntity.ok(yearlyService.reorder(request.getOrderedIds()));
    }
}