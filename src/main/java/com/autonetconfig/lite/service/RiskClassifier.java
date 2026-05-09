package com.autonetconfig.lite.service;

import com.autonetconfig.lite.model.RiskLevel;
import org.springframework.stereotype.Component;

@Component
public class RiskClassifier {
    public RiskLevel classify(String configChange) {
        String change = configChange.toLowerCase();

        if (change.startsWith("delete ") || change.contains("bgp") || change.contains("policy-options")) {
            return RiskLevel.HIGH;
        }
        if (change.contains("routing-options")) {
            return RiskLevel.MEDIUM;
        }
        if (change.contains("interfaces") && change.contains("description")) {
            return RiskLevel.LOW;
        }
        return RiskLevel.MEDIUM;
    }
}
