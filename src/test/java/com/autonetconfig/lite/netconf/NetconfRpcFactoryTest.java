package com.autonetconfig.lite.netconf;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NetconfRpcFactoryTest {
    private final NetconfRpcFactory rpcFactory = new NetconfRpcFactory();

    @Test
    void buildsCommitConfirmedRpcWithTimeout() {
        assertThat(rpcFactory.commitConfirmed(120))
                .contains("<commit>")
                .contains("<confirmed/>")
                .contains("<confirm-timeout>120</confirm-timeout>");
    }

    @Test
    void escapesConfigTextInsideEditConfigRpc() {
        String rpc = rpcFactory.editCandidateConfig("set system login message \"hello & welcome\"");

        assertThat(rpc).contains("<edit-config>");
        assertThat(rpc).contains("&quot;hello &amp; welcome&quot;");
    }
}
