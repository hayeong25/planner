package com.planner.my.service;

import com.planner.my.dto.MonthlyPlanRequest;
import com.planner.my.dto.MonthlyPlanResponse;
import com.planner.my.entity.MonthlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.repository.MonthlyPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonthlyService {

    private final MonthlyPlanRepository monthlyPlanRepository;

    /**
     * 새로운 월간 계획을 생성합니다.
     *
     * @param request 월간 계획 생성 요청
     * @return 생성된 월간 계획 응답
     */
    @Transactional
    public MonthlyPlanResponse create(MonthlyPlanRequest request) {
        Integer maxOrder = monthlyPlanRepository.findMaxDisplayOrder();
        MonthlyPlan plan = MonthlyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .year(request.getYear())
                .month(request.getMonth())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .displayOrder(maxOrder + 1)
                .build();
        return MonthlyPlanResponse.from(monthlyPlanRepository.save(plan));
    }

    /**
     * 모든 월간 계획을 표시 순서대로 조회합니다.
     *
     * @return 월간 계획 목록
     */
    public List<MonthlyPlanResponse> findAll() {
        return monthlyPlanRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    /**
     * ID로 월간 계획을 조회합니다.
     *
     * @param id 조회할 계획 ID
     * @return 월간 계획 응답
     */
    public MonthlyPlanResponse findById(Long id) {
        return monthlyPlanRepository.findById(id)
                .map(MonthlyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Monthly plan not found: " + id));
    }

    /**
     * 연도와 월로 월간 계획을 조회합니다.
     *
     * @param year 연도
     * @param month 월
     * @return 해당 월의 계획 목록
     */
    public List<MonthlyPlanResponse> findByYearAndMonth(Integer year, Integer month) {
        return monthlyPlanRepository.findByYearAndMonthOrderByDisplayOrderAsc(year, month).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    /**
     * 연도별 월간 계획을 조회합니다.
     *
     * @param year 연도
     * @return 해당 연도의 계획 목록
     */
    public List<MonthlyPlanResponse> findByYear(Integer year) {
        return monthlyPlanRepository.findByYearOrderByDisplayOrderAsc(year).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    /**
     * 상태별 월간 계획을 조회합니다.
     *
     * @param status 조회할 상태
     * @return 해당 상태의 월간 계획 목록
     */
    public List<MonthlyPlanResponse> findByStatus(PlanStatus status) {
        return monthlyPlanRepository.findByStatusOrderByDisplayOrderAsc(status).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    /**
     * 우선순위별 월간 계획을 조회합니다.
     *
     * @param priority 조회할 우선순위
     * @return 해당 우선순위의 월간 계획 목록
     */
    public List<MonthlyPlanResponse> findByPriority(Priority priority) {
        return monthlyPlanRepository.findByPriorityOrderByDisplayOrderAsc(priority).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    /**
     * 월간 계획을 수정합니다.
     *
     * @param id 수정할 계획 ID
     * @param request 수정 요청
     * @return 수정된 월간 계획 응답
     */
    @Transactional
    public MonthlyPlanResponse update(Long id, MonthlyPlanRequest request) {
        MonthlyPlan plan = monthlyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monthly plan not found: " + id));

        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setYear(request.getYear());
        plan.setMonth(request.getMonth());
        plan.setPriority(request.getPriority());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        return MonthlyPlanResponse.from(plan);
    }

    /**
     * 월간 계획의 상태를 변경합니다.
     * 완료(COMPLETED) 또는 실패(FAILED) 상태인 계획은 상태를 변경할 수 없습니다.
     *
     * @param id 변경할 계획 ID
     * @param status 새로운 상태
     * @return 수정된 월간 계획 응답
     * @throws IllegalStateException 완료 또는 실패 상태의 계획을 변경하려 할 때
     */
    @Transactional
    public MonthlyPlanResponse updateStatus(Long id, PlanStatus status) {
        MonthlyPlan plan = monthlyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monthly plan not found: " + id));
        validateStatusChange(plan.getStatus());
        plan.setStatus(status);
        return MonthlyPlanResponse.from(plan);
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
     * 월간 계획을 삭제합니다.
     *
     * @param id 삭제할 계획 ID
     */
    @Transactional
    public void delete(Long id) {
        if (!monthlyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Monthly plan not found: " + id);
        }
        monthlyPlanRepository.deleteById(id);
    }

    /**
     * 월간 계획의 표시 순서를 재정렬합니다.
     *
     * @param orderedIds 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 월간 계획 목록
     */
    @Transactional
    public List<MonthlyPlanResponse> reorder(List<Long> orderedIds) {
        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            MonthlyPlan plan = monthlyPlanRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Monthly plan not found: " + id));
            plan.setDisplayOrder(i);
        }
        return findAll();
    }
}
