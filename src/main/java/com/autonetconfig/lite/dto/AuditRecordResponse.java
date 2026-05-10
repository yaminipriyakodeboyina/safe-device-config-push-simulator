package com.autonetconfig.lite.dto;

import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.JobStatus;
import com.autonetconfig.lite.model.NetconfOperation;
import com.autonetconfig.lite.model.RiskLevel;

import java.time.Instant;
import java.util.List;

public class AuditRecordResponse {
    private final String jobId;
    private final String deviceId;
    private final String oldConfig;
    private final String newConfig;
    private final String diff;
    private final RiskLevel riskLevel;
    private final JobStatus finalStatus;
    private final Instant createdAt;
    private final Instant completedAt;
    private final List<NetconfOperation> netconfOperations;

    public AuditRecordResponse(
            String jobId,
            String deviceId,
            String oldConfig,
            String newConfig,
            String diff,
            RiskLevel riskLevel,
            JobStatus finalStatus,
            Instant createdAt,
            Instant completedAt,
            List<NetconfOperation> netconfOperations
    ) {
        this.jobId = jobId;
        this.deviceId = deviceId;
        this.oldConfig = oldConfig;
        this.newConfig = newConfig;
        this.diff = diff;
        this.riskLevel = riskLevel;
        this.finalStatus = finalStatus;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.netconfOperations = netconfOperations;
    }

    public static AuditRecordResponse from(ConfigPushJob job) {
        return new AuditRecordResponse(
                job.getJobId(),
                job.getDeviceId(),
                job.getOldConfig(),
                job.getNewConfig(),
                job.getDiff(),
                job.getRiskLevel(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getCompletedAt(),
                job.getNetconfOperations()
        );
    }

    public String getJobId() {
        return jobId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getOldConfig() {
        return oldConfig;
    }

    public String getNewConfig() {
        return newConfig;
    }

    public String getDiff() {
        return diff;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public JobStatus getFinalStatus() {
        return finalStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public List<NetconfOperation> getNetconfOperations() {
        return netconfOperations;
    }
}
