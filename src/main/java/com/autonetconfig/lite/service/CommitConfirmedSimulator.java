package com.autonetconfig.lite.service;

import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.JobStatus;
import org.springframework.stereotype.Component;

@Component
public class CommitConfirmedSimulator {
    public void stage(ConfigPushJob job) {
        job.setStatus(JobStatus.COMMIT_CONFIRMED_PENDING);
        job.setMessage("Temporary commit staged. Waiting for health checks before final confirmation.");
    }

    public void confirm(ConfigPushJob job) {
        job.setStatus(JobStatus.CONFIRMED);
        job.setMessage("Commit confirmed after health checks passed.");
    }
}
