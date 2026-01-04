package com.planner.my.repository;

import com.planner.my.entity.MonthlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyPlanRepository extends JpaRepository<MonthlyPlan, Long> {
    List<MonthlyPlan> findByYearAndMonth(Integer year, Integer month);

    List<MonthlyPlan> findByYear(Integer year);

    List<MonthlyPlan> findByStatus(PlanStatus status);

    List<MonthlyPlan> findByPriority(Priority priority);
}