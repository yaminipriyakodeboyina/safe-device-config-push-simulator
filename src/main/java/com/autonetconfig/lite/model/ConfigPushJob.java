package com.autonetconfig.lite.model;

import java.time.Instant;

public class ConfigPushJob {
    private final String jobId;
    private final String deviceId;
    private final String configChange;
    private final String changeType;
    private final Instant createdAt;
    private volatile Instant completedAt;
    private volatile JobStatus status;
    private volatile RiskLevel riskLevel;
    private volatile String oldConfig;
    private volatile String newConfig;
    private volatile String diff;
    private volatile String message;

    public ConfigPushJob(String jobId, String deviceId, String configChange, String changeType) {
        this.jobId = jobId;
        this.deviceId = deviceId;
        this.configChange = configChange;
        this.changeType = changeType;
        this.createdAt = Instant.now();
        this.status = JobStatus.VALIDATING;
    }

    public String getJobId() {
        return jobId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getConfigChange() {
        return configChange;
    }

    public String getChangeType() {
        return changeType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getOldConfig() {
        return oldConfig;
    }

    public void setOldConfig(String oldConfig) {
        this.oldConfig = oldConfig;
    }

    public String getNewConfig() {
        return newConfig;
    }

    public void setNewConfig(String newConfig) {
        this.newConfig = newConfig;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
