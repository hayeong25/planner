package com.planner.my.repository;

import com.planner.my.entity.YearlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YearlyPlanRepository extends JpaRepository<YearlyPlan, Long> {
    List<YearlyPlan> findByYear(Integer year);

    List<YearlyPlan> findByStatus(PlanStatus status);

    List<YearlyPlan> findByPriority(Priority priority);
}