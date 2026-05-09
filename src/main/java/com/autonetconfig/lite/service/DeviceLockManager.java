package com.autonetconfig.lite.service;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DeviceLockManager {
    private final Set<String> lockedDevices = ConcurrentHashMap.newKeySet();

    public boolean tryLock(String deviceId) {
        return lockedDevices.add(deviceId);
    }

    public void unlock(String deviceId) {
        lockedDevices.remove(deviceId);
    }

    public boolean isLocked(String deviceId) {
        return lockedDevices.contains(deviceId);
    }
}
