package com.planner.my.service;

import com.planner.my.dto.WeeklyPlanRequest;
import com.planner.my.dto.WeeklyPlanResponse;
import com.planner.my.entity.WeeklyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.repository.WeeklyPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeeklyService {

    private final WeeklyPlanRepository weeklyPlanRepository;

    /**
     * 새로운 주간 계획을 생성합니다.
     *
     * @param request 주간 계획 생성 요청
     * @return 생성된 주간 계획 응답
     */
    @Transactional
    public WeeklyPlanResponse create(WeeklyPlanRequest request) {
        Integer maxOrder = weeklyPlanRepository.findMaxDisplayOrder();
        WeeklyPlan plan = WeeklyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .weekStartDate(request.getWeekStartDate())
                .weekEndDate(request.getWeekEndDate())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .displayOrder(maxOrder + 1)
                .build();
        return WeeklyPlanResponse.from(weeklyPlanRepository.save(plan));
    }

    /**
     * 모든 주간 계획을 표시 순서대로 조회합니다.
     *
     * @return 주간 계획 목록
     */
    public List<WeeklyPlanResponse> findAll() {
        return weeklyPlanRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    /**
     * ID로 주간 계획을 조회합니다.
     *
     * @param id 조회할 계획 ID
     * @return 주간 계획 응답
     */
    public WeeklyPlanResponse findById(Long id) {
        return weeklyPlanRepository.findById(id)
                .map(WeeklyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Weekly plan not found: " + id));
    }

    /**
     * 주 시작일로 주간 계획을 조회합니다.
     *
     * @param weekStartDate 주 시작일
     * @return 해당 주의 계획 목록
     */
    public List<WeeklyPlanResponse> findByWeekStartDate(LocalDate weekStartDate) {
        return weeklyPlanRepository.findByWeekStartDateOrderByDisplayOrderAsc(weekStartDate).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    /**
     * 날짜 범위의 주간 계획을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 주간 계획 목록
     */
    public List<WeeklyPlanResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return weeklyPlanRepository.findByWeekStartDateBetweenOrderByDisplayOrderAsc(startDate, endDate).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    /**
     * 상태별 주간 계획을 조회합니다.
     *
     * @param status 조회할 상태
     * @return 해당 상태의 주간 계획 목록
     */
    public List<WeeklyPlanResponse> findByStatus(PlanStatus status) {
        return weeklyPlanRepository.findByStatusOrderByDisplayOrderAsc(status).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    /**
     * 우선순위별 주간 계획을 조회합니다.
     *
     * @param priority 조회할 우선순위
     * @return 해당 우선순위의 주간 계획 목록
     */
    public List<WeeklyPlanResponse> findByPriority(Priority priority) {
        return weeklyPlanRepository.findByPriorityOrderByDisplayOrderAsc(priority).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    /**
     * 주간 계획을 수정합니다.
     *
     * @param id 수정할 계획 ID
     * @param request 수정 요청
     * @return 수정된 주간 계획 응답
     */
    @Transactional
    public WeeklyPlanResponse update(Long id, WeeklyPlanRequest request) {
        WeeklyPlan plan = weeklyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Weekly plan not found: " + id));

        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setWeekStartDate(request.getWeekStartDate());
        plan.setWeekEndDate(request.getWeekEndDate());
        plan.setPriority(request.getPriority());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        return WeeklyPlanResponse.from(plan);
    }

    /**
     * 주간 계획의 상태를 변경합니다.
     * 완료(COMPLETED) 또는 실패(FAILED) 상태인 계획은 상태를 변경할 수 없습니다.
     *
     * @param id 변경할 계획 ID
     * @param status 새로운 상태
     * @return 수정된 주간 계획 응답
     * @throws IllegalStateException 완료 또는 실패 상태의 계획을 변경하려 할 때
     */
    @Transactional
    public WeeklyPlanResponse updateStatus(Long id, PlanStatus status) {
        WeeklyPlan plan = weeklyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Weekly plan not found: " + id));
        validateStatusChange(plan.getStatus());
        plan.setStatus(status);
        return WeeklyPlanResponse.from(plan);
    }

    /**
     * 상태 변경 가능 여부를 검증합니다.
     *
     * @param currentStatus 현재 상태
     * @throws IllegalStateException 완료 또는 실패 상태일 때
     */
    private void validateStatusChange(PlanStatus currentStatus) {
        if (currentStatus == PlanStatus.COMPLETED || currentStatus == PlanStatus.FAILED) {
            throw new IllegalStateException("완료 또는 실패 상태의 계획은 상태를 변경할 수 없습니다.");
        }
    }

    /**
     * 주간 계획을 삭제합니다.
     *
     * @param id 삭제할 계획 ID
     */
    @Transactional
    public void delete(Long id) {
        if (!weeklyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Weekly plan not found: " + id);
        }
        weeklyPlanRepository.deleteById(id);
    }

    /**
     * 주간 계획의 표시 순서를 재정렬합니다.
     *
     * @param orderedIds 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 주간 계획 목록
     */
    @Transactional
    public List<WeeklyPlanResponse> reorder(List<Long> orderedIds) {
        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            WeeklyPlan plan = weeklyPlanRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Weekly plan not found: " + id));
            plan.setDisplayOrder(i);
        }
        return findAll();
    }
}
