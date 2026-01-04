package com.planner.my.controller;

import com.planner.my.dto.MonthlyPlanRequest;
import com.planner.my.dto.MonthlyPlanResponse;
import com.planner.my.dto.ReorderRequest;
import com.planner.my.dto.StatusUpdateRequest;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.service.MonthlyService;
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
@RequestMapping("/api/monthly")
@RequiredArgsConstructor
@Tag(name = "Monthly Plan", description = "월간 계획 관리 API")
public class MonthlyController {

    private final MonthlyService monthlyService;

    @PostMapping
    @Operation(summary = "월간 계획 생성", description = "새로운 월간 계획을 생성합니다.")
    public ResponseEntity<MonthlyPlanResponse> create(@Valid @RequestBody MonthlyPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monthlyService.create(request));
    }

    @GetMapping
    @Operation(summary = "전체 월간 계획 조회", description = "모든 월간 계획을 조회합니다.")
    public ResponseEntity<List<MonthlyPlanResponse>> findAll() {
        return ResponseEntity.ok(monthlyService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "월간 계획 단건 조회", description = "ID로 특정 월간 계획을 조회합니다.")
    public ResponseEntity<MonthlyPlanResponse> findById(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(monthlyService.findById(id));
    }

    @GetMapping("/year/{year}/month/{month}")
    @Operation(summary = "연/월별 계획 조회", description = "특정 연도와 월의 모든 계획을 조회합니다.")
    public ResponseEntity<List<MonthlyPlanResponse>> findByYearAndMonth(
            @Parameter(description = "연도", example = "2025") @PathVariable Integer year,
            @Parameter(description = "월 (1-12)", example = "12") @PathVariable Integer month) {
        return ResponseEntity.ok(monthlyService.findByYearAndMonth(year, month));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "연도별 월간 계획 조회", description = "특정 연도의 모든 월간 계획을 조회합니다.")
    public ResponseEntity<List<MonthlyPlanResponse>> findByYear(
            @Parameter(description = "연도", example = "2025") @PathVariable Integer year) {
        return ResponseEntity.ok(monthlyService.findByYear(year));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 월간 계획 조회", description = "특정 상태의 모든 월간 계획을 조회합니다.")
    public ResponseEntity<List<MonthlyPlanResponse>> findByStatus(
            @Parameter(description = "계획 상태", example = "NOT_STARTED") @PathVariable PlanStatus status) {
        return ResponseEntity.ok(monthlyService.findByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "우선순위별 월간 계획 조회", description = "특정 우선순위의 모든 월간 계획을 조회합니다.")
    public ResponseEntity<List<MonthlyPlanResponse>> findByPriority(
            @Parameter(description = "우선순위", example = "HIGH") @PathVariable Priority priority) {
        return ResponseEntity.ok(monthlyService.findByPriority(priority));
    }

    @PutMapping("/{id}")
    @Operation(summary = "월간 계획 수정", description = "기존 월간 계획을 수정합니다.")
    public ResponseEntity<MonthlyPlanResponse> update(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody MonthlyPlanRequest request) {
        return ResponseEntity.ok(monthlyService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "월간 계획 상태 변경", description = "계획의 달성 상태만 변경합니다.")
    public ResponseEntity<MonthlyPlanResponse> updateStatus(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(monthlyService.updateStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "월간 계획 삭제", description = "월간 계획을 삭제합니다.")
    public ResponseEntity<Void> delete(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        monthlyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 월간 계획의 표시 순서를 재정렬합니다.
     *
     * @param request 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 월간 계획 목록
     */
    @PutMapping("/reorder")
    @Operation(summary = "월간 계획 순서 변경", description = "드래그 앤 드롭으로 계획 순서를 변경합니다.")
    public ResponseEntity<List<MonthlyPlanResponse>> reorder(@Valid @RequestBody ReorderRequest request) {
        return ResponseEntity.ok(monthlyService.reorder(request.getOrderedIds()));
    }
}