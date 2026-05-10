package com.autonetconfig.lite.netconf;

import com.autonetconfig.lite.model.ConfigPushJob;

public interface DeviceConfigClient {
    void lockCandidate(String deviceId, ConfigPushJob job);

    String getRunningConfig(String deviceId, ConfigPushJob job);

    void editCandidateConfig(String deviceId, String candidateConfig, ConfigPushJob job);

    void validateCandidate(String deviceId, ConfigPushJob job);

    void commitConfirmed(String deviceId, ConfigPushJob job);

    void confirmCommit(String deviceId, ConfigPushJob job);

    void discardCandidate(String deviceId, ConfigPushJob job);

    void unlockCandidate(String deviceId, ConfigPushJob job);
}
