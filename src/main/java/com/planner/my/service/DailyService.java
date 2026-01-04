package com.planner.my.service;

import com.planner.my.dto.DailyPlanRequest;
import com.planner.my.dto.DailyPlanResponse;
import com.planner.my.entity.DailyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.repository.DailyPlanRepository;
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

    @Transactional
    public DailyPlanResponse create(DailyPlanRequest request) {
        DailyPlan plan = DailyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .planDate(request.getPlanDate())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .build();
        return DailyPlanResponse.from(dailyPlanRepository.save(plan));
    }

    public List<DailyPlanResponse> findAll() {
        return dailyPlanRepository.findAll().stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    public DailyPlanResponse findById(Long id) {
        return dailyPlanRepository.findById(id)
                .map(DailyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Daily plan not found: " + id));
    }

    public List<DailyPlanResponse> findByDate(LocalDate date) {
        return dailyPlanRepository.findByPlanDate(date).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    public List<DailyPlanResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyPlanRepository.findByPlanDateBetween(startDate, endDate).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    public List<DailyPlanResponse> findByStatus(PlanStatus status) {
        return dailyPlanRepository.findByStatus(status).stream()
                .map(DailyPlanResponse::from)
                .toList();
    }

    public List<DailyPlanResponse> findByPriority(Priority priority) {
        return dailyPlanRepository.findByPriority(priority).stream()
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

    @Transactional
    public DailyPlanResponse updateStatus(Long id, PlanStatus status) {
        DailyPlan plan = dailyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Daily plan not found: " + id));
        plan.setStatus(status);
        return DailyPlanResponse.from(plan);
    }

    @Transactional
    public void delete(Long id) {
        if (!dailyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Daily plan not found: " + id);
        }
        dailyPlanRepository.deleteById(id);
    }
}