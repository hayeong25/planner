package com.planner.my.repository;

import com.planner.my.entity.MonthlyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 월간 계획 엔티티에 대한 데이터 액세스 레이어입니다.
 */
@Repository
public interface MonthlyPlanRepository extends JpaRepository<MonthlyPlan, Long> {

    /**
     * 모든 월간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<MonthlyPlan> findAllByOrderByDisplayOrderAsc();

    /**
     * 특정 연도와 월의 월간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<MonthlyPlan> findByYearAndMonthOrderByDisplayOrderAsc(Integer year, Integer month);

    /**
     * 특정 연도의 모든 월간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<MonthlyPlan> findByYearOrderByDisplayOrderAsc(Integer year);

    /**
     * 특정 상태의 월간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<MonthlyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    /**
     * 특정 우선순위의 월간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<MonthlyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    /**
     * 현재 최대 표시 순서 값을 조회합니다.
     */
    @Query("SELECT COALESCE(MAX(m.displayOrder), 0) FROM MonthlyPlan m")
    Integer findMaxDisplayOrder();
}