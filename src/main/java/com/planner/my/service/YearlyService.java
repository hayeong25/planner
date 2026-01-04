package com.planner.my.service;

import com.planner.my.dto.YearlyPlanRequest;
import com.planner.my.dto.YearlyPlanResponse;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.entity.YearlyPlan;
import com.planner.my.repository.YearlyPlanRepository;
import com.planner.my.util.PlanStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class YearlyService {

    private final YearlyPlanRepository yearlyPlanRepository;

    /**
     * 새로운 연간 계획을 생성합니다.
     *
     * @param request 연간 계획 생성 요청
     * @return 생성된 연간 계획 응답
     */
    @Transactional
    public YearlyPlanResponse create(YearlyPlanRequest request) {
        Integer maxOrder = yearlyPlanRepository.findMaxDisplayOrder();
        YearlyPlan plan = YearlyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .year(request.getYear())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .displayOrder(maxOrder + 1)
                .build();
        return YearlyPlanResponse.from(yearlyPlanRepository.save(plan));
    }

    /**
     * 모든 연간 계획을 표시 순서대로 조회합니다.
     *
     * @return 연간 계획 목록
     */
    public List<YearlyPlanResponse> findAll() {
        return yearlyPlanRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    /**
     * ID로 연간 계획을 조회합니다.
     *
     * @param id 조회할 계획 ID
     * @return 연간 계획 응답
     */
    public YearlyPlanResponse findById(Long id) {
        return yearlyPlanRepository.findById(id)
                .map(YearlyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Yearly plan not found: " + id));
    }

    /**
     * 연도별 연간 계획을 조회합니다.
     *
     * @param year 연도
     * @return 해당 연도의 계획 목록
     */
    public List<YearlyPlanResponse> findByYear(Integer year) {
        return yearlyPlanRepository.findByYearOrderByDisplayOrderAsc(year).stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    /**
     * 상태별 연간 계획을 조회합니다.
     *
     * @param status 조회할 상태
     * @return 해당 상태의 연간 계획 목록
     */
    public List<YearlyPlanResponse> findByStatus(PlanStatus status) {
        return yearlyPlanRepository.findByStatusOrderByDisplayOrderAsc(status).stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    /**
     * 우선순위별 연간 계획을 조회합니다.
     *
     * @param priority 조회할 우선순위
     * @return 해당 우선순위의 연간 계획 목록
     */
    public List<YearlyPlanResponse> findByPriority(Priority priority) {
        return yearlyPlanRepository.findByPriorityOrderByDisplayOrderAsc(priority).stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    /**
     * 연간 계획을 수정합니다.
     *
     * @param id 수정할 계획 ID
     * @param request 수정 요청
     * @return 수정된 연간 계획 응답
     */
    @Transactional
    public YearlyPlanResponse update(Long id, YearlyPlanRequest request) {
        YearlyPlan plan = yearlyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Yearly plan not found: " + id));

        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setYear(request.getYear());
        plan.setPriority(request.getPriority());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        return YearlyPlanResponse.from(plan);
    }

    /**
     * 연간 계획의 상태를 변경합니다.
     * 완료(COMPLETED) 또는 실패(FAILED) 상태인 계획은 상태를 변경할 수 없습니다.
     *
     * @param id 변경할 계획 ID
     * @param status 새로운 상태
     * @return 수정된 연간 계획 응답
     * @throws IllegalStateException 완료 또는 실패 상태의 계획을 변경하려 할 때
     */
    @Transactional
    public YearlyPlanResponse updateStatus(Long id, PlanStatus status) {
        YearlyPlan plan = yearlyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Yearly plan not found: " + id));
        PlanStatusValidator.validateStatusChange(plan.getStatus());
        plan.setStatus(status);
        return YearlyPlanResponse.from(plan);
    }

    /**
     * 연간 계획을 삭제합니다.
     *
     * @param id 삭제할 계획 ID
     */
    @Transactional
    public void delete(Long id) {
        if (!yearlyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Yearly plan not found: " + id);
        }
        yearlyPlanRepository.deleteById(id);
    }

    /**
     * 연간 계획의 표시 순서를 재정렬합니다.
     *
     * @param orderedIds 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 연간 계획 목록
     */
    @Transactional
    public List<YearlyPlanResponse> reorder(List<Long> orderedIds) {
        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            YearlyPlan plan = yearlyPlanRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Yearly plan not found: " + id));
            plan.setDisplayOrder(i);
        }
        return findAll();
    }
}
