package com.planner.my.repository;

import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 주간 계획 엔티티에 대한 데이터 액세스 레이어입니다.
 */
@Repository
public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {

    /**
     * 모든 주간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<WeeklyPlan> findAllByOrderByDisplayOrderAsc();

    /**
     * 특정 주 시작일의 주간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<WeeklyPlan> findByWeekStartDateOrderByDisplayOrderAsc(LocalDate weekStartDate);

    /**
     * 날짜 범위 내의 주간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<WeeklyPlan> findByWeekStartDateBetweenOrderByDisplayOrderAsc(LocalDate startDate, LocalDate endDate);

    /**
     * 특정 상태의 주간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<WeeklyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    /**
     * 특정 우선순위의 주간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<WeeklyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    /**
     * 현재 최대 표시 순서 값을 조회합니다.
     */
    @Query("SELECT COALESCE(MAX(w.displayOrder), 0) FROM WeeklyPlan w")
    Integer findMaxDisplayOrder();
}