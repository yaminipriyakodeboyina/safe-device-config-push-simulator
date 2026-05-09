package com.autonetconfig.lite.store;

import com.autonetconfig.lite.model.ConfigPushJob;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class ConfigPushJobStore {
    private final ConcurrentMap<String, ConfigPushJob> jobs = new ConcurrentHashMap<>();

    public void save(ConfigPushJob job) {
        jobs.put(job.getJobId(), job);
    }

    public Optional<ConfigPushJob> findById(String jobId) {
        return Optional.ofNullable(jobs.get(jobId));
    }

    public List<ConfigPushJob> findAll() {
        List<ConfigPushJob> result = new ArrayList<>(jobs.values());
        result.sort(Comparator.comparing(ConfigPushJob::getCreatedAt).reversed());
        return result;
    }
}
