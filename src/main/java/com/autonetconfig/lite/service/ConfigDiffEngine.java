package com.autonetconfig.lite.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConfigDiffEngine {
    public String applyChange(String oldConfig, String configChange) {
        Set<String> lines = toLines(oldConfig);
        String normalizedChange = configChange.trim();

        if (normalizedChange.toLowerCase().startsWith("delete ")) {
            String setEquivalent = "set " + normalizedChange.substring("delete ".length()).trim();
            lines.remove(setEquivalent);
        } else {
            replaceStaticRouteWithSamePrefix(lines, normalizedChange);
            lines.add(normalizedChange);
        }

        return String.join("\n", lines);
    }

    public String generateDiff(String oldConfig, String newConfig) {
        Set<String> oldLines = toLines(oldConfig);
        Set<String> newLines = toLines(newConfig);

        String removed = oldLines.stream()
                .filter(line -> !newLines.contains(line))
                .map(line -> "- " + line)
                .collect(Collectors.joining("\n"));

        String added = newLines.stream()
                .filter(line -> !oldLines.contains(line))
                .map(line -> "+ " + line)
                .collect(Collectors.joining("\n"));

        if (removed.isBlank()) {
            return added;
        }
        if (added.isBlank()) {
            return removed;
        }
        return added + "\n" + removed;
    }

    private Set<String> toLines(String config) {
        if (config == null || config.isBlank()) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(config.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void replaceStaticRouteWithSamePrefix(Set<String> lines, String configChange) {
        String marker = "set routing-options static route ";
        String nextHopMarker = " next-hop ";
        if (!configChange.startsWith(marker) || !configChange.contains(nextHopMarker)) {
            return;
        }

        String routePrefix = configChange.substring(0, configChange.indexOf(nextHopMarker));
        lines.removeIf(line -> line.startsWith(routePrefix + nextHopMarker));
    }
}
