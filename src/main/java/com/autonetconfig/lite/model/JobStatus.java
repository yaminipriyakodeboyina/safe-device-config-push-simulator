package com.autonetconfig.lite.model;

public enum JobStatus {
    VALIDATING,
    DEVICE_LOCKED,
    DIFF_GENERATED,
    RISK_CLASSIFIED,
    COMMIT_CONFIRMED_PENDING,
    HEALTH_CHECK_PASSED,
    HEALTH_CHECK_FAILED,
    CONFIRMED,
    ROLLED_BACK,
    FAILED
}
