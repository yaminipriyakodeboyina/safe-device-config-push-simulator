package com.autonetconfig.lite.service;

import org.springframework.stereotype.Component;

@Component
public class ConfigValidator {
    public void validate(String configChange) {
        String normalized = configChange == null ? "" : configChange.trim().toLowerCase();
        if (!(normalized.startsWith("set ") || normalized.startsWith("delete "))) {
            throw new IllegalArgumentException("Config change must start with 'set' or 'delete'.");
        }
        if (normalized.contains("rollback unsafe") || normalized.contains("force-commit")) {
            throw new IllegalArgumentException("Unsafe config command rejected.");
        }
    }
}
