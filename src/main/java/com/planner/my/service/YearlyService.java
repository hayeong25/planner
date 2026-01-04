package com.planner.my.service;

import com.planner.my.dto.YearlyPlanRequest;
import com.planner.my.dto.YearlyPlanResponse;
import com.planner.my.entity.YearlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.repository.YearlyPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class YearlyService {

    private final YearlyPlanRepository yearlyPlanRepository;

    @Transactional
    public YearlyPlanResponse create(YearlyPlanRequest request) {
        YearlyPlan plan = YearlyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .year(request.getYear())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .build();
        return YearlyPlanResponse.from(yearlyPlanRepository.save(plan));
    }

    public List<YearlyPlanResponse> findAll() {
        return yearlyPlanRepository.findAll().stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    public YearlyPlanResponse findById(Long id) {
        return yearlyPlanRepository.findById(id)
                .map(YearlyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Yearly plan not found: " + id));
    }

    public List<YearlyPlanResponse> findByYear(Integer year) {
        return yearlyPlanRepository.findByYear(year).stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    public List<YearlyPlanResponse> findByStatus(PlanStatus status) {
        return yearlyPlanRepository.findByStatus(status).stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

    public List<YearlyPlanResponse> findByPriority(Priority priority) {
        return yearlyPlanRepository.findByPriority(priority).stream()
                .map(YearlyPlanResponse::from)
                .toList();
    }

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

    @Transactional
    public YearlyPlanResponse updateStatus(Long id, PlanStatus status) {
        YearlyPlan plan = yearlyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Yearly plan not found: " + id));
        plan.setStatus(status);
        return YearlyPlanResponse.from(plan);
    }

    @Transactional
    public void delete(Long id) {
        if (!yearlyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Yearly plan not found: " + id);
        }
        yearlyPlanRepository.deleteById(id);
    }
}