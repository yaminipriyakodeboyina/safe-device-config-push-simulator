package com.autonetconfig.lite.dto;

import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.JobStatus;
import com.autonetconfig.lite.model.RiskLevel;

public class ConfigPushResponse {
    private final String jobId;
    private final String deviceId;
    private final JobStatus status;
    private final RiskLevel riskLevel;
    private final String diff;
    private final String message;

    public ConfigPushResponse(String jobId, String deviceId, JobStatus status, RiskLevel riskLevel, String diff, String message) {
        this.jobId = jobId;
        this.deviceId = deviceId;
        this.status = status;
        this.riskLevel = riskLevel;
        this.diff = diff;
        this.message = message;
    }

    public static ConfigPushResponse from(ConfigPushJob job) {
        return new ConfigPushResponse(
                job.getJobId(),
                job.getDeviceId(),
                job.getStatus(),
                job.getRiskLevel(),
                job.getDiff(),
                job.getMessage()
        );
    }

    public String getJobId() {
        return jobId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getDiff() {
        return diff;
    }

    public String getMessage() {
        return message;
    }
}
