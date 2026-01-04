package com.planner.my.repository;

import com.planner.my.entity.YearlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface YearlyPlanRepository extends JpaRepository<YearlyPlan, Long> {
    List<YearlyPlan> findAllByOrderByDisplayOrderAsc();

    List<YearlyPlan> findByYearOrderByDisplayOrderAsc(Integer year);

    List<YearlyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    List<YearlyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    @Query("SELECT COALESCE(MAX(y.displayOrder), 0) FROM YearlyPlan y")
    Integer findMaxDisplayOrder();

    List<YearlyPlan> findByYear(Integer year);

    List<YearlyPlan> findByStatus(PlanStatus status);

    List<YearlyPlan> findByPriority(Priority priority);
}