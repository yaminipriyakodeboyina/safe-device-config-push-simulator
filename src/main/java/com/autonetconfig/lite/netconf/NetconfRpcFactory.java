package com.autonetconfig.lite.netconf;

import org.springframework.stereotype.Component;

@Component
public class NetconfRpcFactory {
    public String lockCandidate() {
        return "<rpc><lock><target><candidate/></target></lock></rpc>";
    }

    public String unlockCandidate() {
        return "<rpc><unlock><target><candidate/></target></unlock></rpc>";
    }

    public String getRunningConfig() {
        return "<rpc><get-config><source><running/></source></get-config></rpc>";
    }

    public String editCandidateConfig(String candidateConfig) {
        return "<rpc><edit-config><target><candidate/></target><config><configuration-text>"
                + escapeXml(candidateConfig)
                + "</configuration-text></config></edit-config></rpc>";
    }

    public String validateCandidate() {
        return "<rpc><validate><source><candidate/></source></validate></rpc>";
    }

    public String commitConfirmed(int timeoutSeconds) {
        return "<rpc><commit><confirmed/><confirm-timeout>"
                + timeoutSeconds
                + "</confirm-timeout></commit></rpc>";
    }

    public String confirmCommit() {
        return "<rpc><commit/></rpc>";
    }

    public String discardChanges() {
        return "<rpc><discard-changes/></rpc>";
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
