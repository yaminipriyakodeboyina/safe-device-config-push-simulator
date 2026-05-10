package com.autonetconfig.lite.netconf;

import com.autonetconfig.lite.model.ConfigPushJob;
import com.autonetconfig.lite.model.NetconfOperation;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SimulatedNetconfDeviceConfigClient implements DeviceConfigClient {
    private static final int COMMIT_CONFIRM_TIMEOUT_SECONDS = 120;

    private final NetconfRpcFactory rpcFactory;
    private final ConcurrentMap<String, String> runningConfigs = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, String> candidateConfigs = new ConcurrentHashMap<>();

    public SimulatedNetconfDeviceConfigClient(NetconfRpcFactory rpcFactory) {
        this.rpcFactory = rpcFactory;
        runningConfigs.put("device-sea-001",
                "set system host-name device-sea-001\n" +
                "set interfaces xe-0/0/0 description uplink-to-core\n" +
                "set routing-options static route 10.0.0.0/24 next-hop 192.168.1.2");
        runningConfigs.put("device-sfo-001",
                "set system host-name device-sfo-001\n" +
                "set interfaces xe-0/0/1 description edge-uplink");
    }

    @Override
    public void lockCandidate(String deviceId, ConfigPushJob job) {
        record(job, "lock-candidate", rpcFactory.lockCandidate());
    }

    @Override
    public String getRunningConfig(String deviceId, ConfigPushJob job) {
        record(job, "get-running-config", rpcFactory.getRunningConfig());
        return runningConfigs.getOrDefault(deviceId, "set system host-name " + deviceId);
    }

    @Override
    public void editCandidateConfig(String deviceId, String candidateConfig, ConfigPushJob job) {
        candidateConfigs.put(deviceId, candidateConfig);
        record(job, "edit-candidate-config", rpcFactory.editCandidateConfig(candidateConfig));
    }

    @Override
    public void validateCandidate(String deviceId, ConfigPushJob job) {
        record(job, "validate-candidate", rpcFactory.validateCandidate());
    }

    @Override
    public void commitConfirmed(String deviceId, ConfigPushJob job) {
        record(job, "commit-confirmed", rpcFactory.commitConfirmed(COMMIT_CONFIRM_TIMEOUT_SECONDS));
    }

    @Override
    public void confirmCommit(String deviceId, ConfigPushJob job) {
        String candidateConfig = candidateConfigs.get(deviceId);
        if (candidateConfig != null) {
            runningConfigs.put(deviceId, candidateConfig);
            candidateConfigs.remove(deviceId);
        }
        record(job, "confirm-commit", rpcFactory.confirmCommit());
    }

    @Override
    public void discardCandidate(String deviceId, ConfigPushJob job) {
        candidateConfigs.remove(deviceId);
        record(job, "discard-candidate", rpcFactory.discardChanges());
    }

    @Override
    public void unlockCandidate(String deviceId, ConfigPushJob job) {
        record(job, "unlock-candidate", rpcFactory.unlockCandidate());
    }

    private void record(ConfigPushJob job, String operation, String rpc) {
        job.addNetconfOperation(new NetconfOperation(operation, rpc));
    }
}
