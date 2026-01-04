package com.planner.my.service;

import com.planner.my.dto.DailyPlanRequest;
import com.planner.my.dto.DailyPlanResponse;
import com.planner.my.entity.DailyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.repository.DailyPlanRepository;
import com.planner.my.util.PlanStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyService {

    private final DailyPlanRepository dailyPlanRepository;

    /**
     * 새로운 일간 계획을 생성합니다.
     *
     * @param request 일간 계획 생성 요청
     * @return 생성된 일간 계획 응답
     */
    @Transactional
    public DailyPlanResponse create(DailyPlanRequest request) {
        Integer maxOrder = dailyPlanRepository.findMaxDisplayOrder();
        DailyPlan plan = DailyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .planDate(request.getPlanDate())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .displayOrder(maxOrder + 1)
                .build();
        return DailyPlanResponse.from(dailyPlanRepository.save(plan));
    }

    /**
     * 모든 일간 계획을 표시 순서대로 조회합니다.
     *
     * @return 일간 계획 목록
     */
    public List<DailyPlanResponse> findAll() {
        return dailyPlanRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    public DailyPlanResponse findById(Long id) {
        return dailyPlanRepository.findById(id)
                .map(DailyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Daily plan not found: " + id));
    }

    /**
     * 특정 날짜의 일간 계획을 조회합니다.
     *
     * @param date 조회할 날짜
     * @return 해당 날짜의 일간 계획 목록
     */
    public List<DailyPlanResponse> findByDate(LocalDate date) {
        return dailyPlanRepository.findByPlanDateOrderByDisplayOrderAsc(date).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    /**
     * 날짜 범위의 일간 계획을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 일간 계획 목록
     */
    public List<DailyPlanResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyPlanRepository.findByPlanDateBetweenOrderByDisplayOrderAsc(startDate, endDate).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    /**
     * 상태별 일간 계획을 조회합니다.
     *
     * @param status 조회할 상태
     * @return 해당 상태의 일간 계획 목록
     */
    public List<DailyPlanResponse> findByStatus(PlanStatus status) {
        return dailyPlanRepository.findByStatusOrderByDisplayOrderAsc(status).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    /**
     * 우선순위별 일간 계획을 조회합니다.
     *
     * @param priority 조회할 우선순위
     * @return 해당 우선순위의 일간 계획 목록
     */
    public List<DailyPlanResponse> findByPriority(Priority priority) {
        return dailyPlanRepository.findByPriorityOrderByDisplayOrderAsc(priority).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    @Transactional
    public DailyPlanResponse update(Long id, DailyPlanRequest request) {
        DailyPlan plan = dailyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Daily plan not found: " + id));

        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        plan.setPlanDate(request.getPlanDate());
        plan.setPriority(request.getPriority());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        return DailyPlanResponse.from(plan);
    }

    /**
     * 일간 계획의 상태를 변경합니다.
     * 완료(COMPLETED) 또는 실패(FAILED) 상태인 계획은 상태를 변경할 수 없습니다.
     *
     * @param id 변경할 계획 ID
     * @param status 새로운 상태
     * @return 수정된 일간 계획 응답
     * @throws IllegalStateException 완료 또는 실패 상태의 계획을 변경하려 할 때
     */
    @Transactional
    public DailyPlanResponse updateStatus(Long id, PlanStatus status) {
        DailyPlan plan = dailyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Daily plan not found: " + id));
        PlanStatusValidator.validateStatusChange(plan.getStatus());
        plan.setStatus(status);
        return DailyPlanResponse.from(plan);
    }

    /**
     * 일간 계획을 삭제합니다.
     *
     * @param id 삭제할 계획 ID
     */
    @Transactional
    public void delete(Long id) {
        if (!dailyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Daily plan not found: " + id);
        }
        dailyPlanRepository.deleteById(id);
    }

    /**
     * 일간 계획의 표시 순서를 재정렬합니다.
     *
     * @param orderedIds 새로운 순서대로 정렬된 계획 ID 목록
     * @return 재정렬된 일간 계획 목록
     */
    @Transactional
    public List<DailyPlanResponse> reorder(List<Long> orderedIds) {
        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            DailyPlan plan = dailyPlanRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Daily plan not found: " + id));
            plan.setDisplayOrder(i);
        }
        return findAll();
    }
}