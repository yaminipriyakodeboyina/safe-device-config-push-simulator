package com.autonetconfig.lite.netconf;

import com.autonetconfig.lite.model.ConfigPushJob;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimulatedNetconfDeviceConfigClientTest {
    private final SimulatedNetconfDeviceConfigClient client =
            new SimulatedNetconfDeviceConfigClient(new NetconfRpcFactory());

    @Test
    void recordsNetconfOperationsForCandidateWorkflow() {
        ConfigPushJob job = new ConfigPushJob(
                "job-1",
                "device-sea-001",
                "set interfaces xe-0/0/0 description test",
                "interface"
        );

        client.lockCandidate(job.getDeviceId(), job);
        String runningConfig = client.getRunningConfig(job.getDeviceId(), job);
        client.editCandidateConfig(job.getDeviceId(), runningConfig + "\nset system services netconf ssh", job);
        client.validateCandidate(job.getDeviceId(), job);
        client.commitConfirmed(job.getDeviceId(), job);
        client.confirmCommit(job.getDeviceId(), job);
        client.unlockCandidate(job.getDeviceId(), job);

        assertThat(job.getNetconfOperations())
                .extracting("operation")
                .containsExactly(
                        "lock-candidate",
                        "get-running-config",
                        "edit-candidate-config",
                        "validate-candidate",
                        "commit-confirmed",
                        "confirm-commit",
                        "unlock-candidate"
                );
    }
}
