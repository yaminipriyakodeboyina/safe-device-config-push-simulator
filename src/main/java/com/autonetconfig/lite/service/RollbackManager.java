package com.autonetconfig.lite.service;

import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.JobStatus;
import com.autonetconfig.lite.netconf.DeviceConfigClient;
import org.springframework.stereotype.Component;

@Component
public class RollbackManager {
    private final DeviceConfigClient deviceConfigClient;

    public RollbackManager(DeviceConfigClient deviceConfigClient) {
        this.deviceConfigClient = deviceConfigClient;
    }

    public void rollback(ConfigPushJob job) {
        deviceConfigClient.discardCandidate(job.getDeviceId(), job);
        job.setStatus(JobStatus.ROLLED_BACK);
        job.setMessage("Health checks failed. Rolled back to previous config.");
    }
}
