package com.autonetconfig.lite.model;

import java.time.Instant;

public class NetconfOperation {
    private final String operation;
    private final String rpc;
    private final Instant executedAt;

    public NetconfOperation(String operation, String rpc) {
        this.operation = operation;
        this.rpc = rpc;
        this.executedAt = Instant.now();
    }

    public String getOperation() {
        return operation;
    }

    public String getRpc() {
        return rpc;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }
}
