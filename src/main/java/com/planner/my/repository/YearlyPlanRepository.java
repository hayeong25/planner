package com.planner.my.repository;

import com.planner.my.entity.PlanStatus;
import com.planner.my.entity.Priority;
import com.planner.my.entity.YearlyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 연간 계획 엔티티에 대한 데이터 액세스 레이어입니다.
 */
@Repository
public interface YearlyPlanRepository extends JpaRepository<YearlyPlan, Long> {

    /**
     * 모든 연간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<YearlyPlan> findAllByOrderByDisplayOrderAsc();

    /**
     * 특정 연도의 연간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<YearlyPlan> findByYearOrderByDisplayOrderAsc(Integer year);

    /**
     * 특정 상태의 연간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<YearlyPlan> findByStatusOrderByDisplayOrderAsc(PlanStatus status);

    /**
     * 특정 우선순위의 연간 계획을 표시 순서로 정렬하여 조회합니다.
     */
    List<YearlyPlan> findByPriorityOrderByDisplayOrderAsc(Priority priority);

    /**
     * 현재 최대 표시 순서 값을 조회합니다.
     */
    @Query("SELECT COALESCE(MAX(y.displayOrder), 0) FROM YearlyPlan y")
    Integer findMaxDisplayOrder();
}