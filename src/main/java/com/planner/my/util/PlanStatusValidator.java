package com.planner.my.util;

import com.planner.my.entity.PlanStatus;

/**
 * 계획 상태 관련 검증 유틸리티 클래스입니다.
 */
public final class PlanStatusValidator {

    private PlanStatusValidator() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    /**
     * 상태 변경 가능 여부를 검증합니다.
     * 완료(COMPLETED) 또는 실패(FAILED) 상태인 경우 상태를 변경할 수 없습니다.
     *
     * @param currentStatus 현재 상태
     * @throws IllegalStateException 완료 또는 실패 상태일 때
     */
    public static void validateStatusChange(PlanStatus currentStatus) {
        if (isFinalized(currentStatus)) {
            throw new IllegalStateException("완료 또는 실패 상태의 계획은 상태를 변경할 수 없습니다.");
        }
    }

    /**
     * 해당 상태가 최종 상태(완료 또는 실패)인지 확인합니다.
     *
     * @param status 확인할 상태
     * @return 최종 상태이면 true, 아니면 false
     */
    public static boolean isFinalized(PlanStatus status) {
        return status == PlanStatus.COMPLETED || status == PlanStatus.FAILED;
    }
}
