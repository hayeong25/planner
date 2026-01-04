package com.planner.my.repository;

import com.planner.my.entity.DailyPlan;
import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 일간 계획 엔티티에 대한 데이터 액세스 레이어입니다.
 */
@Repository
public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

    /**
     * 모든 일간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<DailyPlan> findAllByOrderByDisplayOrderAsc();

    /**
     * 특정 날짜의 일간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<DailyPlan> findByPlanDateOrderByDisplayOrderAsc(LocalDate planDate);

    /**
     * 날짜 범위 내의 일간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<DailyPlan> findByPlanDateBetweenOrderByDisplayOrderAsc(LocalDate startDate, LocalDate endDate);

    /**
     * 특정 상태의 일간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<DailyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    /**
     * 특정 우선순위의 일간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<DailyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    /**
     * 현재 최대 표시 순서 값을 조회합니다.
     */
    @Query("SELECT COALESCE(MAX(d.displayOrder), 0) FROM DailyPlan d")
    Integer findMaxDisplayOrder();
}