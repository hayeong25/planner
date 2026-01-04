package com.planner.my.repository;

import com.planner.my.entity.DailyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {
    List<DailyPlan> findByPlanDate(LocalDate planDate);

    List<DailyPlan> findByPlanDateBetween(LocalDate startDate, LocalDate endDate);

    List<DailyPlan> findByStatus(PlanStatus status);

    List<DailyPlan> findByPriority(Priority priority);
}