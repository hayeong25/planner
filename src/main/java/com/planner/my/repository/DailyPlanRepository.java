package com.planner.my.repository;

import com.planner.my.entity.DailyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {
    List<DailyPlan> findAllByOrderByDisplayOrderAsc();

    List<DailyPlan> findByPlanDateOrderByDisplayOrderAsc(LocalDate planDate);

    List<DailyPlan> findByPlanDateBetweenOrderByDisplayOrderAsc(LocalDate startDate, LocalDate endDate);

    List<DailyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    List<DailyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    @Query("SELECT COALESCE(MAX(d.displayOrder), 0) FROM DailyPlan d")
    Integer findMaxDisplayOrder();

    List<DailyPlan> findByPlanDate(LocalDate planDate);

    List<DailyPlan> findByPlanDateBetween(LocalDate startDate, LocalDate endDate);

    List<DailyPlan> findByStatus(PlanStatus status);

    List<DailyPlan> findByPriority(Priority priority);
}