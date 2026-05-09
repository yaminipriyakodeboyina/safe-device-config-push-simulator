package com.autonetconfig.lite.service;

import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.JobStatus;
import com.autonetconfig.lite.store.DeviceConfigStore;
import org.springframework.stereotype.Component;

@Component
public class RollbackManager {
    private final DeviceConfigStore deviceConfigStore;

    public RollbackManager(DeviceConfigStore deviceConfigStore) {
        this.deviceConfigStore = deviceConfigStore;
    }

    public void rollback(ConfigPushJob job) {
        deviceConfigStore.putConfig(job.getDeviceId(), job.getOldConfig());
        job.setStatus(JobStatus.ROLLED_BACK);
        job.setMessage("Health checks failed. Rolled back to previous config.");
    }
}
