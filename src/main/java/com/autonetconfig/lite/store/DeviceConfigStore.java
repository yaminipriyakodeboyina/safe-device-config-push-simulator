package com.autonetconfig.lite.store;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class DeviceConfigStore {
    private final ConcurrentMap<String, String> deviceConfigs = new ConcurrentHashMap<>();

    public DeviceConfigStore() {
        deviceConfigs.put("device-sea-001",
                "set system host-name device-sea-001\n" +
                "set interfaces xe-0/0/0 description uplink-to-core\n" +
                "set routing-options static route 10.0.0.0/24 next-hop 192.168.1.2");
        deviceConfigs.put("device-sfo-001",
                "set system host-name device-sfo-001\n" +
                "set interfaces xe-0/0/1 description edge-uplink");
    }

    public String getConfig(String deviceId) {
        return deviceConfigs.getOrDefault(deviceId, "set system host-name " + deviceId);
    }

    public void putConfig(String deviceId, String config) {
        deviceConfigs.put(deviceId, config);
    }
}
