package com.autonetconfig.lite.service;

public class DeviceLockedException extends RuntimeException {
    public DeviceLockedException(String deviceId) {
        super("Device " + deviceId + " already has a running config push.");
    }
}
