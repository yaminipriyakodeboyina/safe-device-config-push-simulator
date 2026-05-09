package com.autonetconfig.lite.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigDiffEngineTest {
    private final ConfigDiffEngine diffEngine = new ConfigDiffEngine();

    @Test
    void generatesReadableAddedAndRemovedLines() {
        String oldConfig = "set system host-name device-sea-001\n" +
                "set routing-options static route 10.0.0.0/24 next-hop 192.168.1.2";
        String newConfig = "set system host-name device-sea-001\n" +
                "set routing-options static route 10.0.0.0/24 next-hop 192.168.1.1";

        String diff = diffEngine.generateDiff(oldConfig, newConfig);

        assertThat(diff).contains("+ set routing-options static route 10.0.0.0/24 next-hop 192.168.1.1");
        assertThat(diff).contains("- set routing-options static route 10.0.0.0/24 next-hop 192.168.1.2");
    }

    @Test
    void replacingStaticRouteNextHopRemovesPreviousRouteLine() {
        String oldConfig = "set system host-name device-sea-001\n" +
                "set routing-options static route 10.0.0.0/24 next-hop 192.168.1.2";

        String newConfig = diffEngine.applyChange(
                oldConfig,
                "set routing-options static route 10.0.0.0/24 next-hop 192.168.1.1"
        );

        assertThat(newConfig).contains("next-hop 192.168.1.1");
        assertThat(newConfig).doesNotContain("next-hop 192.168.1.2");
    }
}
