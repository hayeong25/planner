package com.planner.my.controller;

import com.planner.my.dto.DailyPlanRequest;
import com.planner.my.dto.DailyPlanResponse;
import com.planner.my.dto.ReorderRequest;
import com.planner.my.dto.StatusUpdateRequest;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.service.DailyService;
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
@RequestMapping("/api/daily")
@RequiredArgsConstructor
@Tag(name = "Daily Plan", description = "일간 계획 관리 API")
public class DailyController {

    private final DailyService dailyService;

    @PostMapping
    @Operation(summary = "일간 계획 생성", description = "새로운 일간 계획을 생성합니다.")
    public ResponseEntity<DailyPlanResponse> create(@Valid @RequestBody DailyPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dailyService.create(request));
    }

    @GetMapping
    @Operation(summary = "전체 일간 계획 조회", description = "모든 일간 계획을 조회합니다.")
    public ResponseEntity<List<DailyPlanResponse>> findAll() {
        return ResponseEntity.ok(dailyService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "일간 계획 단건 조회", description = "ID로 특정 일간 계획을 조회합니다.")
    public ResponseEntity<DailyPlanResponse> findById(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(dailyService.findById(id));
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "날짜별 일간 계획 조회", description = "특정 날짜의 모든 계획을 조회합니다.")
    public ResponseEntity<List<DailyPlanResponse>> findByDate(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", example = "2025-12-21")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(dailyService.findByDate(date));
    }

    @GetMapping("/date-range")
    @Operation(summary = "기간별 일간 계획 조회", description = "시작일부터 종료일까지의 모든 계획을 조회합니다.")
    public ResponseEntity<List<DailyPlanResponse>> findByDateRange(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)", example = "2025-12-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)", example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dailyService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 일간 계획 조회", description = "특정 상태의 모든 계획을 조회합니다.")
    public ResponseEntity<List<DailyPlanResponse>> findByStatus(
            @Parameter(description = "계획 상태", example = "NOT_STARTED") @PathVariable PlanStatus status) {
        return ResponseEntity.ok(dailyService.findByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "우선순위별 일간 계획 조회", description = "특정 우선순위의 모든 계획을 조회합니다.")
    public ResponseEntity<List<DailyPlanResponse>> findByPriority(
            @Parameter(description = "우선순위", example = "HIGH") @PathVariable Priority priority) {
        return ResponseEntity.ok(dailyService.findByPriority(priority));
    }

    @PutMapping("/{id}")
    @Operation(summary = "일간 계획 수정", description = "기존 일간 계획을 수정합니다.")
    public ResponseEntity<DailyPlanResponse> update(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody DailyPlanRequest request) {
        return ResponseEntity.ok(dailyService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "일간 계획 상태 변경", description = "계획의 달성 상태만 변경합니다.")
    public ResponseEntity<DailyPlanResponse> updateStatus(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(dailyService.updateStatus(id, request.getStatus()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "일간 계획 삭제", description = "일간 계획을 삭제합니다.")
    public ResponseEntity<Void> delete(
            @Parameter(description = "계획 ID", example = "1") @PathVariable Long id) {
        dailyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 일간 계획의 표시 순서를 재정렬합니다.
     *
     * @param request 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 일간 계획 목록
     */
    @PutMapping("/reorder")
    @Operation(summary = "일간 계획 순서 변경", description = "드래그 앤 드롭으로 계획 순서를 변경합니다.")
    public ResponseEntity<List<DailyPlanResponse>> reorder(@Valid @RequestBody ReorderRequest request) {
        return ResponseEntity.ok(dailyService.reorder(request.getOrderedIds()));
    }
}