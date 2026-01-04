package com.planner.my.repository;

import com.planner.my.entity.MonthlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface MonthlyPlanRepository extends JpaRepository<MonthlyPlan, Long> {
    List<MonthlyPlan> findAllByOrderByDisplayOrderAsc();

    List<MonthlyPlan> findByYearAndMonthOrderByDisplayOrderAsc(Integer year, Integer month);

    List<MonthlyPlan> findByYearOrderByDisplayOrderAsc(Integer year);

    List<MonthlyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    List<MonthlyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    @Query("SELECT COALESCE(MAX(m.displayOrder), 0) FROM MonthlyPlan m")
    Integer findMaxDisplayOrder();

    List<MonthlyPlan> findByYearAndMonth(Integer year, Integer month);

    List<MonthlyPlan> findByYear(Integer year);

    List<MonthlyPlan> findByStatus(PlanStatus status);

    List<MonthlyPlan> findByPriority(Priority priority);
}