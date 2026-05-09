package com.autonetconfig.lite.service;

import com.autonetconfig.lite.model.RiskLevel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RiskClassifierTest {
    private final RiskClassifier classifier = new RiskClassifier();

    @Test
    void classifiesInterfaceDescriptionAsLowRisk() {
        assertThat(classifier.classify("set interfaces xe-0/0/0 description customer-uplink"))
                .isEqualTo(RiskLevel.LOW);
    }

    @Test
    void classifiesRoutingChangeAsMediumRisk() {
        assertThat(classifier.classify("set routing-options static route 10.0.0.0/24 next-hop 192.168.1.1"))
                .isEqualTo(RiskLevel.MEDIUM);
    }

    @Test
    void classifiesDeleteAndPolicyChangesAsHighRisk() {
        assertThat(classifier.classify("delete routing-options static route 10.0.0.0/24"))
                .isEqualTo(RiskLevel.HIGH);
        assertThat(classifier.classify("set policy-options policy-statement export-direct then accept"))
                .isEqualTo(RiskLevel.HIGH);
    }
}
