package com.planner.my.controller;

import com.planner.my.dto.WeeklyPlanRequest;
import com.planner.my.dto.WeeklyPlanResponse;
import com.planner.my.dto.StatusUpdateRequest;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.service.WeeklyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weekly")
@RequiredArgsConstructor
@Tag(name = "Weekly Plan", description = "주간 계획 관리 API")
public class WeeklyController {

    private final WeeklyService weeklyService;

    @PostMapping
    @Operation(summary = "주간 계획 생성", description = "새로운 주간 계획을 생성합니다.")
    public ResponseEntity<WeeklyPlanResponse> create(@Valid @RequestBody WeeklyPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(weeklyService.create(request));
    }

    @GetMapping
    @Operation(summary = "전체 주간 계획 조회", description = "모든 주간 계획을 조회합니다.")
    public ResponseEntity<List<WeeklyPlanResponse>> findAll() {
        return ResponseEntity.ok(weeklyService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "주간 계획 단건 조회", description = "ID로 특정 주간 계획을 조회합니다.")
    public ResponseEntity<WeeklyPlanResponse> findById(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(weeklyService.findById(id));
    }

    @GetMapping("/week/{weekStartDate}")
    @Operation(summary = "주 시작일별 계획 조회", description = "특정 주 시작일의 모든 계획을 조회합니다.")
    public ResponseEntity<List<WeeklyPlanResponse>> findByWeekStartDate(
            @Parameter(description = "주 시작 날짜 (yyyy-MM-dd)", example = "2025-12-15")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate) {
        return ResponseEntity.ok(weeklyService.findByWeekStartDate(weekStartDate));
    }

    @GetMapping("/date-range")
    @Operation(summary = "기간별 주간 계획 조회", description = "시작일부터 종료일 사이에 시작하는 모든 주간 계획을 조회합니다.")
    public ResponseEntity<List<WeeklyPlanResponse>> findByDateRange(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)", example = "2025-12-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)", example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(weeklyService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 주간 계획 조회", description = "특정 상태의 모든 주간 계획을 조회합니다.")
    public ResponseEntity<List<WeeklyPlanResponse>> findByStatus(
            @Parameter(description = "계획 상태", example = "NOT_STARTED") @PathVariable PlanStatus status) {
        return ResponseEntity.ok(weeklyService.findByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "우선순위별 주간 계획 조회", description = "특정 우선순위의 모든 주간 계획을 조회합니다.")
    public ResponseEntity<List<WeeklyPlanResponse>> findByPriority(
            @Parameter(description = "우선순위", example = "HIGH") @PathVariable Priority priority) {
        return ResponseEntity.ok(weeklyService.findByPriority(priority));
    }

    @PutMapping("/{id}")
    @Operation(summary = "주간 계획 수정", description = "기존 주간 계획을 수정합니다.")
    public ResponseEntity<WeeklyPlanResponse> update(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody WeeklyPlanRequest request) {
        return ResponseEntity.ok(weeklyService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "주간 계획 상태 변경", description = "계획의 달성 상태만 변경합니다.")
    public ResponseEntity<WeeklyPlanResponse> updateStatus(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(weeklyService.updateStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "주간 계획 삭제", description = "주간 계획을 삭제합니다.")
    public ResponseEntity<Void> delete(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        weeklyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}