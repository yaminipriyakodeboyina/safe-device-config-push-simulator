package com.autonetconfig.lite.dto;

import javax.validation.constraints.NotBlank;

public class ConfigPushRequest {
    @NotBlank
    private String deviceId;

    @NotBlank
    private String configChange;

    @NotBlank
    private String changeType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getConfigChange() {
        return configChange;
    }

    public void setConfigChange(String configChange) {
        this.configChange = configChange;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
}
