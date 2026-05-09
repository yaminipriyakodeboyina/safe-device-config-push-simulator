package com.autonetconfig.lite.service;

import com.autonetconfig.lite.dto.ConfigPushRequest;
import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.JobStatus;
import com.autonetconfig.lite.model.RiskLevel;
import com.autonetconfig.lite.store.ConfigPushJobStore;
import com.autonetconfig.lite.store.DeviceConfigStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ConfigPushOrchestrator {
    private final ConfigValidator validator;
    private final DeviceLockManager lockManager;
    private final ConfigDiffEngine diffEngine;
    private final RiskClassifier riskClassifier;
    private final CommitConfirmedSimulator commitConfirmedSimulator;
    private final HealthCheckEngine healthCheckEngine;
    private final RollbackManager rollbackManager;
    private final DeviceConfigStore deviceConfigStore;
    private final ConfigPushJobStore jobStore;

    public ConfigPushOrchestrator(
            ConfigValidator validator,
            DeviceLockManager lockManager,
            ConfigDiffEngine diffEngine,
            RiskClassifier riskClassifier,
            CommitConfirmedSimulator commitConfirmedSimulator,
            HealthCheckEngine healthCheckEngine,
            RollbackManager rollbackManager,
            DeviceConfigStore deviceConfigStore,
            ConfigPushJobStore jobStore
    ) {
        this.validator = validator;
        this.lockManager = lockManager;
        this.diffEngine = diffEngine;
        this.riskClassifier = riskClassifier;
        this.commitConfirmedSimulator = commitConfirmedSimulator;
        this.healthCheckEngine = healthCheckEngine;
        this.rollbackManager = rollbackManager;
        this.deviceConfigStore = deviceConfigStore;
        this.jobStore = jobStore;
    }

        public ConfigPushJob submit(ConfigPushRequest request) {
        validator.validate(request.getConfigChange());

        if (!lockManager.tryLock(request.getDeviceId())) {
            throw new DeviceLockedException(request.getDeviceId());
        }

        ConfigPushJob job = new ConfigPushJob(
                UUID.randomUUID().toString(),
                request.getDeviceId(),
                request.getConfigChange(),
                request.getChangeType()
        );
        job.setStatus(JobStatus.DEVICE_LOCKED);
        job.setMessage("Device locked. Safe config workflow started.");
        jobStore.save(job);

        CompletableFuture.runAsync(() -> runWorkflow(job));
        return job;
    }

    public Optional<ConfigPushJob> findJob(String jobId) {
        return jobStore.findById(jobId);
    }

    public List<ConfigPushJob> auditLog() {
        return jobStore.findAll();
    }

    private void runWorkflow(ConfigPushJob job) {
        try {
            pauseForReadableStatus();

            String oldConfig = deviceConfigStore.getConfig(job.getDeviceId());
            String newConfig = diffEngine.applyChange(oldConfig, job.getConfigChange());
            String diff = diffEngine.generateDiff(oldConfig, newConfig);
            job.setOldConfig(oldConfig);
            job.setNewConfig(newConfig);
            job.setDiff(diff);
            job.setStatus(JobStatus.DIFF_GENERATED);

            RiskLevel riskLevel = riskClassifier.classify(job.getConfigChange());
            job.setRiskLevel(riskLevel);
            job.setStatus(JobStatus.RISK_CLASSIFIED);

            commitConfirmedSimulator.stage(job);
            boolean healthy = healthCheckEngine.passes(job.getConfigChange(), riskLevel);

            if (healthy) {
                deviceConfigStore.putConfig(job.getDeviceId(), newConfig);
                job.setStatus(JobStatus.HEALTH_CHECK_PASSED);
                commitConfirmedSimulator.confirm(job);
            } else {
                job.setStatus(JobStatus.HEALTH_CHECK_FAILED);
                rollbackManager.rollback(job);
            }
        } catch (Exception exception) {
            job.setStatus(JobStatus.FAILED);
            job.setMessage(exception.getMessage());
        } finally {
            job.setCompletedAt(Instant.now());
            lockManager.unlock(job.getDeviceId());
            jobStore.save(job);
        }
    }

    private void pauseForReadableStatus() {
        try {
            Thread.sleep(350);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
