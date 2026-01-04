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

    @Transactional
    public WeeklyPlanResponse create(WeeklyPlanRequest request) {
        WeeklyPlan plan = WeeklyPlan.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .weekStartDate(request.getWeekStartDate())
                .weekEndDate(request.getWeekEndDate())
                .priority(request.getPriority())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.NOT_STARTED)
                .build();
        return WeeklyPlanResponse.from(weeklyPlanRepository.save(plan));
    }

    public List<WeeklyPlanResponse> findAll() {
        return weeklyPlanRepository.findAll().stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    public WeeklyPlanResponse findById(Long id) {
        return weeklyPlanRepository.findById(id)
                .map(WeeklyPlanResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Weekly plan not found: " + id));
    }

    public List<WeeklyPlanResponse> findByWeekStartDate(LocalDate weekStartDate) {
        return weeklyPlanRepository.findByWeekStartDate(weekStartDate).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    public List<WeeklyPlanResponse> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return weeklyPlanRepository.findByWeekStartDateBetween(startDate, endDate).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    public List<WeeklyPlanResponse> findByStatus(PlanStatus status) {
        return weeklyPlanRepository.findByStatus(status).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

    public List<WeeklyPlanResponse> findByPriority(Priority priority) {
        return weeklyPlanRepository.findByPriority(priority).stream()
                .map(WeeklyPlanResponse::from)
                .toList();
    }

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

    @Transactional
    public WeeklyPlanResponse updateStatus(Long id, PlanStatus status) {
        WeeklyPlan plan = weeklyPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Weekly plan not found: " + id));
        plan.setStatus(status);
        return WeeklyPlanResponse.from(plan);
    }

    @Transactional
    public void delete(Long id) {
        if (!weeklyPlanRepository.existsById(id)) {
            throw new IllegalArgumentException("Weekly plan not found: " + id);
        }
        weeklyPlanRepository.deleteById(id);
    }
}