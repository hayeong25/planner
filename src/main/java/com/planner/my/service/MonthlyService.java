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

    @Transactional
    public MonthlyPlanResponse create(MonthlyPlanRequest request) {
        MonthlyPlan plan = MonthlyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .year(request.getYear())
                .month(request.getMonth())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .build();
        return MonthlyPlanResponse.from(monthlyPlanRepository.save(plan));
    }

    public List<MonthlyPlanResponse> findAll() {
        return monthlyPlanRepository.findAll().stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    public MonthlyPlanResponse findById(Long id) {
        return monthlyPlanRepository.findById(id)
                .map(MonthlyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Monthly plan not found: " + id));
    }

    public List<MonthlyPlanResponse> findByYearAndMonth(Integer year, Integer month) {
        return monthlyPlanRepository.findByYearAndMonth(year, month).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    public List<MonthlyPlanResponse> findByYear(Integer year) {
        return monthlyPlanRepository.findByYear(year).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    public List<MonthlyPlanResponse> findByStatus(PlanStatus status) {
        return monthlyPlanRepository.findByStatus(status).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

    public List<MonthlyPlanResponse> findByPriority(Priority priority) {
        return monthlyPlanRepository.findByPriority(priority).stream()
                .map(MonthlyPlanResponse::from)
                .toList();
    }

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

    @Transactional
    public MonthlyPlanResponse updateStatus(Long id, PlanStatus status) {
        MonthlyPlan plan = monthlyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Monthly plan not found: " + id));
        plan.setStatus(status);
        return MonthlyPlanResponse.from(plan);
    }

    @Transactional
    public void delete(Long id) {
        if (!monthlyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Monthly plan not found: " + id);
        }
        monthlyPlanRepository.deleteById(id);
    }
}