package com.planner.my.repository;

import com.planner.my.entity.WeeklyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {
    List<WeeklyPlan> findAllByOrderByDisplayOrderAsc();

    List<WeeklyPlan> findByWeekStartDateOrderByDisplayOrderAsc(LocalDate weekStartDate);

    List<WeeklyPlan> findByWeekStartDateBetweenOrderByDisplayOrderAsc(LocalDate startDate, LocalDate endDate);

    List<WeeklyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    List<WeeklyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    @Query("SELECT COALESCE(MAX(w.displayOrder), 0) FROM WeeklyPlan w")
    Integer findMaxDisplayOrder();

    List<WeeklyPlan> findByWeekStartDate(LocalDate weekStartDate);

    List<WeeklyPlan> findByWeekStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<WeeklyPlan> findByStatus(PlanStatus status);

    List<WeeklyPlan> findByPriority(Priority priority);
}