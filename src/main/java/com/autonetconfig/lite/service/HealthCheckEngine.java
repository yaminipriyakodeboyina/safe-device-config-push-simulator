package com.autonetconfig.lite.service;

import com.autonetconfig.lite.model.RiskLevel;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckEngine {
    public boolean passes(String configChange, RiskLevel riskLevel) {
        String change = configChange.toLowerCase();
        if (change.contains("blackhole") || change.contains("fail-health-check")) {
            return false;
        }
        return riskLevel != RiskLevel.HIGH || !change.startsWith("delete ");
    }
}
